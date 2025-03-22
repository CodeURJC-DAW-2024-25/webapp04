package es.grupo04.backend.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;

import es.grupo04.backend.dto.EditUserDTO;
import es.grupo04.backend.dto.NewReviewDTO;
import es.grupo04.backend.dto.ProductDTO;
import es.grupo04.backend.dto.ReviewDTO;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.dto.UserDTO;
import es.grupo04.backend.service.ChartData;
import es.grupo04.backend.service.ProductService;
import es.grupo04.backend.service.ReviewService;
import es.grupo04.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/profile")

public class ProfileRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @DeleteMapping("/{id}")
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

        // A user that is not adin can only delete their own account
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

    @PutMapping
    public ResponseEntity<?> updateProfile(HttpServletRequest request,
            @RequestParam String name,
            @RequestParam String address,
            @RequestParam String newPass,
            @RequestParam String repeatPass,
            @RequestParam(required = false) MultipartFile profilePicInput,  
            @RequestParam(required = false) String iframe) throws IOException {

        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        Optional<UserBasicDTO> currentUserOptional = userService.findByMail(principal.getName());

        if (currentUserOptional.isEmpty()) {
            return ResponseEntity.status(403).body("Unauthorized");
        }
        
        EditUserDTO updatedUser = new EditUserDTO(currentUserOptional.get().id(), principal.getName(), name, address, newPass, repeatPass, iframe, profilePicInput, currentUserOptional.get().hasImage());
        
        if (profilePicInput != null && !profilePicInput.isEmpty()) {
            userService.saveProfilePic(currentUserOptional.get(), profilePicInput);  // Método para guardar la imagen
        }

        Optional<String> message = userService.editProfile(currentUserOptional.get(), updatedUser);
        if (message.isPresent()) {
            return ResponseEntity.status(400).body(message.get());
        }
        
        return ResponseEntity.ok("Profile updated successfully");
    }

    @GetMapping("/image/{id}")
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
}