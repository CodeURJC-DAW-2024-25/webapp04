package es.grupo04.backend.controller;

import java.net.URI;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.grupo04.backend.dto.NewUserDTO;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.dto.UserDTO;
import es.grupo04.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Operation (summary= "Retrieve details of the authenticated user")
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

    @Operation (summary= "Retrieve a list of all users")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers().stream()
                .map(user -> userService.getUserDTO(user.id()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @Operation (summary= "Retrieve user by its ID")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        Optional<UserDTO> userOptional = userService.findByIdExtendedInfo(userId);
        if (userOptional.isEmpty() || userId == 1) {
            throw new NoSuchElementException("User with not found");
        }
        return ResponseEntity.ok(userOptional.get());
    }

    @Operation (summary= "Create new user")
    @PostMapping("/signin")
    public ResponseEntity<?> createUser(@RequestBody NewUserDTO user) {
        if (!userService.validateUser(user)) {
            return ResponseEntity.badRequest().body("Error in data validation: " + user);
        }

        Optional<UserDTO> userOptional = userService.createAccount(user);

        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequestUri()
            .replacePath(String.format("/api/v1/users/%d", userOptional.get().id()))
            .build()
            .toUri();
        return ResponseEntity.created(location).body(userOptional.get());
    }

}
