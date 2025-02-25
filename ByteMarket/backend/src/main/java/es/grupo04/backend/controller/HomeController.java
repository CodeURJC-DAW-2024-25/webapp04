package es.grupo04.backend.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.User;
import es.grupo04.backend.service.ProductService;
import es.grupo04.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {

            model.addAttribute("logged", true);
            model.addAttribute("userName", principal.getName());
            model.addAttribute("admin", request.isUserInRole("ADMIN"));

        } else {
            model.addAttribute("logged", false);
        }
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        // TODO hay que quitar la lógica y meterla en el service
        // El profe me ha dicho que esto está mal lo de las categorías
        // Obtener todas las categorías únicas de los productos
        List<String> categories = productService.findAll()
                .stream()
                .map(Product::getCategory)
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("navbar_home", "Inicio");
        model.addAttribute("categories", categories);
        model.addAttribute("other_products", productService.findAll());
        model.addAttribute("featured_products", productService.findAll());
        model.addAttribute("carrusel", true);
        model.addAttribute("featured_title", "Productos Destacados");
        model.addAttribute("other_products_title", "Otros Productos");

        if (userDetails != null) {
            model.addAttribute("logged", true);
            model.addAttribute("userName", userDetails.getUsername());

            // Verificar roles
            boolean isAdmin = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equals("ROLE_ADMIN"));

            if (isAdmin) {
                model.addAttribute("admin", true);
                model.addAttribute("adminName", userDetails.getUsername());
            } else {
                model.addAttribute("user", true);
            }

        } else {
            model.addAttribute("logged", false);
        }

        if (userDetails != null) {
            System.out.println("Usuario autenticado: " + userDetails.getUsername());
        } else {
            System.out.println("No hay usuario autenticado.");
        }

        return "home_template";
    }

    @GetMapping("/profile")
    public String userProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";  // O la ruta que desees
        }
        
        Optional<User> optionalUser = userService.findByName(userDetails.getUsername());
        if (!optionalUser.isPresent()) {
            return "redirect:/login";  // Redirigir si el usuario no se encuentra
        }
        
        User user = optionalUser.get();
        
        model.addAttribute("username", user.getName());
        model.addAttribute("profilePic", "");
        model.addAttribute("location", "");
        model.addAttribute("salesNumber", "");
        model.addAttribute("purchasesNumber", "");
        model.addAttribute("reviewsNumber", "");

        return "profile_template";
    }
}

    /*
     * PASAR A PRODUCTCONTROLLER
     * 
     * @GetMapping("/product/{id}")
     * public String getProduct(@PathVariable("id") Long id, Model model) {
     * Optional<Product> productOptional = productRepository.findById(id);
     * 
     * if (productOptional.isEmpty()) {
     * model.addAttribute("message", "Producto no encontrado"); // Pasar un mensaje
     * de error
     * return "error"; // Página de error si no se encuentra el producto
     * }
     * 
     * model.addAttribute("product", productOptional.get());
     * return "productDetail_template";
     * }
     */