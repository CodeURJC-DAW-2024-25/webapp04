package es.grupo04.backend.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import es.grupo04.backend.model.User;
import es.grupo04.backend.service.ProductService;
import es.grupo04.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class ProfileController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {

            User user = userService.findByMail(principal.getName()).get();

            model.addAttribute("logged", true);
            model.addAttribute("userName", user.getName());    
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
            model.addAttribute("user", user);

        } else {
            model.addAttribute("logged", false);
        }
    }

    @GetMapping("/profile")
    public String userProfile(@RequestParam(value = "filter", required = false) String filter, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";  // O la ruta que desees
        }
        
        Optional<User> optionalUser = userService.findByMail(userDetails.getUsername());
        if (!optionalUser.isPresent()) {
            return "redirect:/login";  // Redirigir si el usuario no se encuentra
        }

        boolean showProfileSection = filter == null;
        model.addAttribute("showProfileSection", showProfileSection);
        
        User user = optionalUser.get();

        if (user.getIframe() != null){
            model.addAttribute("location", user.getIframe());
        }
        
        model.addAttribute("username", user.getName());
        model.addAttribute("salesNumber", user.getSales().size());
        model.addAttribute("purchasesNumber", user.getPurchases().size());
        model.addAttribute("reviewsNumber", user.getReviews().size());

        if ("favorites".equals(filter)) {
            model.addAttribute("show_products", user.getFavoriteProducts());
            model.addAttribute("title", "Mis favoritos");
        } else if ("historyPurchase".equals(filter)) {
            model.addAttribute("show_products", productService.getLastPurchases(user));
            model.addAttribute("title", "Últimas compras");
        } else if ("historySales".equals(filter)) {
            model.addAttribute("show_products", productService.getLastSales(user));
            model.addAttribute("title", "Últimas ventas");
        } else {
            model.addAttribute("show_products", productService.findByOwner(user));
            model.addAttribute("title", "Mis productos");
        }

        return "profile_template";
    }

    @GetMapping("/editProfile")
    public String editProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        
        Optional<User> optionalUser = userService.findByMail(userDetails.getUsername());
        if (!optionalUser.isPresent()) {
            model.addAttribute("message", "Usuario no encontrado");
            return "error";
        }

        User user = optionalUser.get();
        model.addAttribute("user", user);
        return "editProfile";
    }

    @PostMapping("/editProfile")
    public String postEditProfile(@AuthenticationPrincipal UserDetails userDetails, Model model, @ModelAttribute User user,
        @RequestParam(name = "newPass", required = false) String newPass, @RequestParam(name = "repeatPass", required = false) String repeatPass,
        @RequestParam(name = "profilePicInput", required = false) MultipartFile profilePic, @RequestParam(name = "iframe", required = false) String iframe
        ) throws IOException {
        
        Optional<User> optionalUser = userService.findByMail(userDetails.getUsername());
        if (!optionalUser.isPresent()) {
            model.addAttribute("message", "Usuario no encontrado: " + userDetails.getUsername());
            return "error";
        }

        if(!profilePic.isEmpty()){
            userService.saveProfilePic(optionalUser.get(), profilePic);
        }

        Optional<String> message = userService.editProfile(user, optionalUser.get(), newPass, repeatPass, iframe);
        if(message.isPresent()) {
            model.addAttribute("message", message.get());
            return "error";
        }

        return "redirect:/profile";
        
    }

    @GetMapping("/user/image/{id}")
    public ResponseEntity<Object> getProductImage(@PathVariable Long id, Model model) throws SQLException {
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Blob image = userOptional.get().getImageFile();
        Resource file = new InputStreamResource(image.getBinaryStream());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .contentLength(image.length()).body(file);

    }
}
