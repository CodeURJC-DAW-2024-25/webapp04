package es.grupo04.backend.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.grupo04.backend.dto.NewUserDTO;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.dto.UserDTO;
import es.grupo04.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }
        Optional<UserBasicDTO> userOptional = userService.findByMail(principal.getName());
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
        
        return ResponseEntity.ok(userOptional.get());
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers().stream()
                .map(user -> userService.getUserDTO(user.id()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        Optional<UserDTO> user = userService.findByIdExtendedInfo(userId);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }


    @PostMapping("/signin")
    public ResponseEntity<?> createUser(@ModelAttribute NewUserDTO user) {
        if (!userService.validateUser(user)) {
            return ResponseEntity.badRequest().body("Error in data validation");
        }

        Optional<UserDTO> userOptional = userService.createAccount(user);

        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        return ResponseEntity.ok(userOptional.get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long userId, HttpServletRequest request) {
        Optional<UserBasicDTO> userOptional = userService.findById(userId);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        UserBasicDTO userToDelete = userOptional.get();
        Principal principal = request.getUserPrincipal();
        if(principal == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        if (!request.isUserInRole("ADMIN")) {
            if (!userToDelete.id().equals(userService.findByMail(principal.getName()).get().id())) {
                return ResponseEntity.badRequest().body("You can only delete your own account");
            }
        }

        userService.delete(userToDelete);
        return ResponseEntity.ok("User deleted successfully");
    }
}
