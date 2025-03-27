package es.grupo04.backend.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.grupo04.backend.dto.EditUserDTO;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.dto.UserDTO;
import es.grupo04.backend.service.ChartData;
import es.grupo04.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/v1/users/{id}")

public class ProfileRestController {

    @Autowired
    private UserService userService;

    @Operation (summary= "Delete a user by its ID")
    @DeleteMapping
    public ResponseEntity<?> deleteAccount(@PathVariable Long id, HttpServletRequest request) {
        Optional<UserBasicDTO> userOptional = userService.findById(id);

        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("User not found");
        }

        UserBasicDTO userToDelete = userOptional.get();
        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        Optional<UserBasicDTO> currentUserOptional = userService.findByMail(principal.getName());

        if (currentUserOptional.isEmpty()) {
            return ResponseEntity.status(403).body("Unauthorized");
        }

        UserBasicDTO currentUser = currentUserOptional.get();

        boolean isCurrentUserAdmin = request.isUserInRole("ADMIN");
        boolean isTargetUserAdmin = userService.hasRole(userToDelete.id(), "ADMIN");

        // A user that is not adim can only delete their own account
        if (!isCurrentUserAdmin) {
            if (!currentUser.id().equals(userToDelete.id())) {
                return ResponseEntity.status(403).body("You can only delete your own account");
            }
        } else {
            // An admin cannot delete another admin but can delete a regular user
            if (isTargetUserAdmin) {
                return ResponseEntity.status(403).body("Admins cannot delete other admins");
            }
        }
        userService.delete(userToDelete);
        return ResponseEntity.ok("User deleted successfully");
    }

    @Operation (summary= "Update user profile information")
    @PutMapping
    public ResponseEntity<?> updateProfile(HttpServletRequest request, @RequestBody EditUserDTO updatedUser) throws IOException {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        Optional<UserBasicDTO> currentUserOptional = userService.findByMail(principal.getName());
        if (currentUserOptional.isEmpty()) {
            return ResponseEntity.status(403).body("Unauthorized");
        }
        
        System.out.println("Mi usuario a editar es:" + currentUserOptional.get());
        System.out.println("Mi usuario editado sería:" + updatedUser);
        Optional<String> message = userService.editProfile(currentUserOptional.get(), updatedUser);
        if (message.isPresent()) {
            return ResponseEntity.status(400).body(message.get());
        }
        
        Optional<UserBasicDTO> updated = userService.findByMail(principal.getName());
        return ResponseEntity.ok(updated.get());
    }


    @PostMapping("/image")
    public ResponseEntity<Void> addImage(@RequestParam MultipartFile image,
            HttpServletRequest request) throws IOException {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserBasicDTO userDTO = userService.findByMail(principal.getName()).get();
        userService.saveProfilePic(userDTO, image);
        return ResponseEntity.ok().build();

    }

    @Operation (summary= "Retrieve a user image by user ID")
    @GetMapping("/image")
    public ResponseEntity<Object> getProfileImage(@PathVariable Long id) throws SQLException, IOException {
        Optional<UserDTO> userOptional = userService.findByIdExtendedInfo(id);
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("User with id " + id + " not found");
        }
        Blob image = userService.getImage(userOptional.get());

        Resource file = new InputStreamResource(image.getBinaryStream());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .contentLength(image.length())
                .body(file);
    }

    @Operation (summary= "Retrieve user's purchases and sales statistics")
    @GetMapping("/stats")
    public ResponseEntity<?> stats(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<UserBasicDTO> optionalUser = userService.findByMail(userDetails.getUsername());
        if (!optionalUser.isPresent()) {
            throw new NoSuchElementException("User with id not found");
        }
        List<ChartData> data = userService.getStats(optionalUser.get());

        return ResponseEntity.ok(data);
    }
}