package es.grupo04.backend.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

@Controller
public class ProfileController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {

            UserBasicDTO user = userService.findByMail(principal.getName()).get();

            model.addAttribute("logged", true);
            model.addAttribute("userName", user.name());    
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
            model.addAttribute("user", user);

        } else {
            model.addAttribute("logged", false);
        }
    }

    @GetMapping("/profile")
    public String userProfile(
            @RequestParam(value = "filter", required = false) String filter,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        int pageSize = 8;
        Optional<UserDTO> optionalUser = userService.findByMailExtendedInfo(userDetails.getUsername());

        boolean showProfileSection = filter == null;
        model.addAttribute("showProfileSection", showProfileSection);

        UserDTO user = optionalUser.get();
        model.addAttribute("id", user.id());

        if (user.iframe() != null) {
            model.addAttribute("location", user.iframe());
        }

        model.addAttribute("isOwnProfile", true);
        model.addAttribute("username", user.name());
        model.addAttribute("salesNumber", user.sales().size());
        model.addAttribute("purchasesNumber", user.purchases().size());
        model.addAttribute("reviewsNumber", user.reviews().size());
        model.addAttribute("reviewsSection", false);

        if ("favorites".equals(filter)) {
            Page<ProductDTO> productPage = productService.getFavoriteProducts(user, page, pageSize);
            model.addAttribute("show_products", productPage.getContent());
            model.addAttribute("title", "Mis favoritos");
            model.addAttribute("filter", "favorites");
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productPage.getTotalPages());
        } else if ("historyPurchase".equals(filter)) {
            model.addAttribute("show_products", productService.getLastPurchases(user)); 
            model.addAttribute("title", "Últimas compras");
            model.addAttribute("filter", "historyPurchase");
            model.addAttribute("renderStats", true);
            model.addAttribute("currentPage", 0); 
            model.addAttribute("totalPages", 1);
        } else if ("historySales".equals(filter)) {
            model.addAttribute("show_products", productService.getLastSales(user)); 
            model.addAttribute("title", "Últimas ventas");
            model.addAttribute("filter", "historySales");
            model.addAttribute("renderStats", true);
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", 1);
        } else if ("reviews".equals(filter)) {
            List<ReviewDTO> reviews = user.reviews();
            model.addAttribute("reviewsSection", true);

            if (reviews == null || reviews.isEmpty()) {
                model.addAttribute("reviews", false);
            } else {
                model.addAttribute("reviews", true);
            }

            List<Map<String, Object>> reviewStars = new ArrayList<>();
            for (ReviewDTO review : reviews) {
                int rating = review.rating();
                List<Boolean> stars = new ArrayList<>();
                List<Boolean> emptyStars = new ArrayList<>();

                for (int i = 0; i < rating; i++) {
                    stars.add(true);
                }

                for (int i = rating; i < 5; i++) {
                    emptyStars.add(false);
                }

                Map<String, Object> reviewStarData = new HashMap<>();
                reviewStarData.put("id", review.id());  
                reviewStarData.put("rating", rating);
                reviewStarData.put("stars", stars);
                reviewStarData.put("emptyStars", emptyStars);
                reviewStarData.put("owner", review.reviewOwner().name());
                reviewStarData.put("description", review.description());
                reviewStars.add(reviewStarData);
            }

            model.addAttribute("reviewStars", reviewStars);
            model.addAttribute("title", "Mis Reseñas");
        } else {
            Page<ProductDTO> productPage = productService.findProductsByOwner(user, page, pageSize);
            model.addAttribute("show_products", productPage.getContent()); 
            model.addAttribute("title", "Mis productos");
            model.addAttribute("filter", "products");
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productPage.getTotalPages());
        }

        return "profile_template";
    }


    @GetMapping("/adminProfile")
    public String adminProfile(@RequestParam(value = "filter", required = false) String filter, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";  
        }
        
        Optional<UserBasicDTO> optionalUser = userService.findByMail(userDetails.getUsername());
        if (!optionalUser.isPresent()) {
            return "redirect:/login";  
        }

        boolean showProfileSection = filter == null;
        model.addAttribute("showProfileSection", showProfileSection);
        
        UserBasicDTO user = optionalUser.get();
        model.addAttribute("id", user.id());
        model.addAttribute("isOwnProfile", true);
        model.addAttribute("username", user.name());

        return "adminProfile_template";
    }
    
    @GetMapping("/profile/{profileId}")
    public String userProfile(@PathVariable Long profileId, @RequestParam(value = "filter", required = false) String filter, @AuthenticationPrincipal UserDetails userDetails, @RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        int pageSize = 8;
        boolean isOwnProfile = false;
        Optional<UserDTO> profileOptional = userService.findByIdExtendedInfo(profileId);
        if(!profileOptional.isPresent() || profileId == 1){     //profileId 1 reserved to manage deleted users
            model.addAttribute("message", "No se encuentra el perfil");
            model.addAttribute("user", null);
            return "error";
        }
        UserDTO profileUser = profileOptional.get();
        if (userDetails == null) {
            isOwnProfile = false;
            model.addAttribute("user", null);
        }else{
            Optional<UserBasicDTO> optionalUser = userService.findByMail(userDetails.getUsername());
            UserBasicDTO user = optionalUser.get();
            isOwnProfile = user.equals(profileUser);
            model.addAttribute("user", user);
        }
        boolean showProfileSection = filter == null;
        model.addAttribute("showProfileSection", showProfileSection);

        if (profileUser.iframe() != null){
            model.addAttribute("location", profileUser.iframe());
        }
        if(!isOwnProfile){
            model.addAttribute("image", profileUser.profileImage());
            model.addAttribute("id", profileUser.id());
        }
        model.addAttribute("isOwnProfile", isOwnProfile);
        model.addAttribute("username", profileUser.name());
        model.addAttribute("salesNumber", profileUser.sales().size());
        model.addAttribute("purchasesNumber", profileUser.purchases().size());
        model.addAttribute("reviewsNumber", profileUser.reviews().size());

        if ("reviews".equals(filter)) {
            List<ReviewDTO> reviews = profileUser.reviews();
            model.addAttribute("reviewsSection", true);

            if (reviews == null || reviews.isEmpty()) {
                model.addAttribute("reviews", false);
            } else {
                model.addAttribute("reviews", true);
            }

            List<Map<String, Object>> reviewStars = new ArrayList<>();

            for (ReviewDTO review : reviews) {
                int rating = review.rating();
                List<Boolean> stars = new ArrayList<>();
                List<Boolean> emptyStars = new ArrayList<>();

                for (int i = 0; i < rating; i++) {
                    stars.add(true);
                }

                for (int i = rating; i < 5; i++) {
                    emptyStars.add(false);
                }

                Map<String, Object> reviewStarData = new HashMap<>();
                reviewStarData.put("id", review.id()); 
                reviewStarData.put("rating", rating);
                reviewStarData.put("stars", stars);
                reviewStarData.put("emptyStars", emptyStars);
                reviewStarData.put("owner", review.reviewOwner().name());
                reviewStarData.put("description", review.description());
                reviewStars.add(reviewStarData);
            }

            model.addAttribute("reviewStars", reviewStars);

            model.addAttribute("reviewStars", reviewStars);
            model.addAttribute("title", "Reseñas");
        }
         else {
            Page<ProductDTO> productPage = productService.findProductsByOwner(profileUser, page, pageSize);
            model.addAttribute("show_products", productPage.getContent()); 
            model.addAttribute("title", "Productos");
            model.addAttribute("filter", "products");
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productPage.getTotalPages());
        }

        return "profile_template";
    }

    @GetMapping("/editProfile")
    public String editProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        
        Optional<UserBasicDTO> optionalUser = userService.findByMail(userDetails.getUsername());
        if (!optionalUser.isPresent()) {
            model.addAttribute("message", "Usuario no encontrado");
            return "error";
        }

        UserBasicDTO user = optionalUser.get();
        model.addAttribute("user", user);
        return "editProfile";
    }

    @PostMapping("/editProfile")
    public String postEditProfile(@AuthenticationPrincipal UserDetails userDetails, Model model, @ModelAttribute EditUserDTO user,
        @RequestParam(name = "newPass", required = false) String newPass, @RequestParam(name = "repeatPass", required = false) String repeatPass,
        @RequestParam(name = "profilePicInput", required = false) MultipartFile profilePic, @RequestParam(name = "iframe", required = false) String iframe
        ) throws IOException {

        Optional<UserBasicDTO> optionalUser = userService.findByMail(userDetails.getUsername());
        if (!optionalUser.isPresent()) {
            model.addAttribute("message", "Usuario no encontrado: " + userDetails.getUsername());
            return "error";
        }

        if(!profilePic.isEmpty()){
            userService.saveProfilePic(optionalUser.get(), profilePic);
        }

        Optional<String> message = userService.editProfile(user);
        if(message.isPresent()) {
            model.addAttribute("message", message.get());
            return "error";
        }

        return "redirect:/profile";
    }

    
    @GetMapping("/user/image/{id}")
    public ResponseEntity<Object> getProductImage(@PathVariable Long id, Model model) throws SQLException {
        Optional<UserDTO> userOptional = userService.findByIdExtendedInfo(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String imageURL = userOptional.get().profileImage();

        if (imageURL == null || imageURL.isEmpty()) {
            return ResponseEntity.notFound().build();  
        }
        return ResponseEntity.status(302)  
        .header(HttpHeaders.LOCATION, imageURL)  
        .build();
    }

    @PostMapping("/deleteAccount")
    public String deleteAccount(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        
        Optional<UserBasicDTO> optionalUser = userService.findByMail(userDetails.getUsername());
        if (!optionalUser.isPresent()) {
            model.addAttribute("message", "Usuario no encontrado");
            return "error";
        }

        userService.delete(optionalUser.get());
        return "/logout";
    }

    @GetMapping("/stats/get")
    public ResponseEntity<?> stats(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        
        Optional<UserBasicDTO> optionalUser = userService.findByMail(userDetails.getUsername());
        if (!optionalUser.isPresent()) {
            model.addAttribute("message", "Usuario no encontrado");
            return null;
        }

        List<ChartData> data = userService.getStats(optionalUser.get());

        return ResponseEntity.ok(data);
    }

    @PostMapping("/product/{id}/newReview")
    public String addReview(@PathVariable Long id, 
                            @RequestParam("rating") int rating, 
                            @RequestParam("description") String description, 
                            HttpServletRequest request) {
        
        Principal principal = request.getUserPrincipal();
        UserBasicDTO reviewOwner = userService.findByMail(principal.getName()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ProductDTO product = productService.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        UserBasicDTO reviewedUser = product.owner();

        NewReviewDTO newReviewDTO = new NewReviewDTO(rating, description, reviewedUser.id());

        reviewService.saveReview(newReviewDTO, reviewOwner.id()); 

        return "redirect:/product/" + id;
    }

    @PostMapping("/review/{id}/delete")
    public String deleteReview(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        System.out.println("Intentando eliminar la reseña con ID: " + id);
        Optional<UserBasicDTO> optionalUser = userService.findByMail(userDetails.getUsername());

        if (!optionalUser.isPresent()) {
            model.addAttribute("message", "Usuario no encontrado");
            return "error";
        }

        
        if(reviewService.getReviewById(id) == null){
            model.addAttribute("message", "Review no encontrada");
            return "error";
        }

        reviewService.delete(id);
        return "redirect:/";  
    }


    @GetMapping("/reviews")
    public String getReviews(Model model) {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        model.addAttribute("title", "Reseñas");
        return "reviews";
    }



}