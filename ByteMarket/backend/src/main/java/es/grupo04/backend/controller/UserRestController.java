package es.grupo04.backend.controller;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.grupo04.backend.dto.NewUserDTO;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.dto.UserDTO;
import es.grupo04.backend.service.UserService;

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
        Optional<UserDTO> userOptional = userService.findByIdExtendedInfo(userId);
        if (userOptional.isEmpty() || userId == 1) {
            throw new NoSuchElementException("User with not found");
        }
        return ResponseEntity.ok(userOptional.get());
    }


    @PostMapping("/signin")
    public ResponseEntity<?> createUser(@RequestBody NewUserDTO user) {
        if (!userService.validateUser(user)) {
            return ResponseEntity.badRequest().body("Error in data validation");
        }

        Optional<UserDTO> userOptional = userService.createAccount(user);

        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        return ResponseEntity.ok(userOptional.get());
    }

}
