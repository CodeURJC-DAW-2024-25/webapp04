package es.grupo04.backend.controller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.grupo04.backend.dto.ChatDTO;
import es.grupo04.backend.dto.EditUserDTO;
import es.grupo04.backend.dto.NewFavoriteDTO;
import es.grupo04.backend.dto.NewUserDTO;
import es.grupo04.backend.dto.NewPurchaseDTO;
import es.grupo04.backend.dto.ProductDTO;
import es.grupo04.backend.dto.PurchaseDTO;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.dto.UserDTO;
import es.grupo04.backend.service.ChartData;
import es.grupo04.backend.service.ChatService;
import es.grupo04.backend.service.ProductService;
import es.grupo04.backend.service.PurchaseService;
import es.grupo04.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ChatService chatService;


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

    @Operation (summary= "Retrieve a list of favorite products of the user")
    @GetMapping("{userId}/favorites")
    public ResponseEntity<Page<ProductDTO>> getFavoriteProducts(
            HttpServletRequest request,
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserDTO userDTO = userService.findByMailExtendedInfo(principal.getName()).get();

        if(userId != userDTO.id()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<ProductDTO> productsPage = productService.getFavoriteProducts(userDTO, page, size);
        return ResponseEntity.ok(productsPage);
    }
    
    @Operation (summary= "Mark a product as favorite by its ID")
    @PostMapping("/{userId}/favorites")
    public ResponseEntity<Page<ProductDTO>> addToFavorites(HttpServletRequest request,
            @PathVariable Long userId,
            @RequestBody NewFavoriteDTO favoriteDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserDTO userDTO = userService.findByMailExtendedInfo(principal.getName()).get();
        if(userId != userDTO.id()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Long productId = favoriteDTO.productId();
        Optional<ProductDTO> productOpt = productService.findById(productId);
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    
        ProductDTO productDTO = productOpt.get();
    
        if (productDTO.owner().id() == userDTO.id()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        boolean isFavorite = userService.isFavorite(userDTO, productId);
        if(!isFavorite){
            userService.addToFavorite(productId, userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        //returns the updated list of favorite products
        Page<ProductDTO> productsPage = productService.getFavoriteProducts(userDTO, page, size);
        return ResponseEntity.ok(productsPage);
    }

    @Operation (summary= "Delete a product from favorites by its ID")
    @DeleteMapping("/{userId}/favorites/{productId}")
    public ResponseEntity<Page<ProductDTO>> deleteFromFavorites(HttpServletRequest request,
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserDTO userDTO = userService.findByMailExtendedInfo(principal.getName()).get();
        if(userId != userDTO.id()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<ProductDTO> productOpt = productService.findById(productId);
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    
        ProductDTO productDTO = productOpt.get();
    
        if (productDTO.owner().id() == userDTO.id()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
        boolean isFavorite = userService.isFavorite(userDTO, productId);
        if(isFavorite){
            userService.removeFromFavorite(productId, userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        //returns the updated list of favorite products
        Page<ProductDTO> productsPage = productService.getFavoriteProducts(userDTO, page, size);
        return ResponseEntity.ok(productsPage);
    }

    @Operation (summary= "Delete a user by its ID")
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
    @PutMapping("/{id}")
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

    @PostMapping("/{id}/image")
    public ResponseEntity<Void> addImage(@RequestParam MultipartFile image,
            HttpServletRequest request) throws IOException {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserBasicDTO userDTO = userService.findByMail(principal.getName()).get();
        userService.saveProfilePic(userDTO, image);
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequestUri()
            .build()
            .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation (summary= "Retrieve a user image by user ID")
    @GetMapping("/{id}/image")
    public ResponseEntity<?> getProfileImage(@PathVariable Long id) throws SQLException, IOException {
        Optional<UserDTO> userOptional = userService.findByIdExtendedInfo(id);
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("User with id " + id + " not found");
        }
        
        if (!userOptional.get().hasImage()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + id + " has no image");
        }

        Blob image = userService.getImage(userOptional.get());
        Resource file = new InputStreamResource(image.getBinaryStream());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .contentLength(image.length())
                .body(file);
    }

    @Operation (summary= "Retrieve user's purchases and sales statistics")
    @GetMapping("/{id}/stats")
    public ResponseEntity<?> stats(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<UserBasicDTO> optionalUser = userService.findByMail(userDetails.getUsername());
        if (!optionalUser.isPresent()) {
            throw new NoSuchElementException("User with id not found");
        }
        List<ChartData> data = userService.getStats(optionalUser.get());

        return ResponseEntity.ok(data);
    }
    
    @Operation(summary = "Mark a product as sold in a chat by its ID")
    @PostMapping("/{userID}/purchases")
    public ResponseEntity<?> sellProduct(@PathVariable Long userID, @RequestBody NewPurchaseDTO purchaseDTO, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        UserBasicDTO seller = userService.findByMail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        if (!userID.equals(seller.id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        ChatDTO chat = chatService.findChatById(purchaseDTO.chatID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat no encontrado"));
        
        if (!chat.userSeller().equals(seller)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para vender este producto");
        }

        if (chat.product().sold()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto ya está vendido");
        }
        
        PurchaseDTO purchase = purchaseService.createPurchase(chat);
        if (purchase == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo completar la venta");
        }
        
        return ResponseEntity.ok(purchase);
    }

    @Operation(summary = "Get purchases filtered by user role")
    @GetMapping("/{userID}/purchases")
    public ResponseEntity<List<PurchaseDTO>> getPurchases(@PathVariable Long userID, @RequestParam(required = false) String role, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        UserBasicDTO loggedInUser = userService.findByMail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        if (!userID.equals(loggedInUser.id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<PurchaseDTO> purchases;
        if ("seller".equalsIgnoreCase(role)) {
            purchases = purchaseService.getLastSales(userID);
        } else if ("buyer".equalsIgnoreCase(role)) {
            purchases = purchaseService.getLastPurchases(userID);
        } else {
            purchases = purchaseService.findAllUserPurchases(userID);
        }
        
        return ResponseEntity.ok(purchases);
    }

}
