package es.grupo04.backend.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.User;
import es.grupo04.backend.service.ProductService;
import es.grupo04.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
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
    public String home(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        // Mostrar 8 productos por página
        int pageSize = 8;
        List<Product> products = productService.findPaginated(page, pageSize); // Implementar paginación en el servicio
        model.addAttribute("other_products", products);
        model.addAttribute("currentPage", page);
        return "home_template";
    }

    @GetMapping("/profile")
    public String userProfile(@RequestParam(value = "filter", required = false) String filter, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";  // O la ruta que desees
        }
        
        Optional<User> optionalUser = userService.findByName(userDetails.getUsername());
        if (!optionalUser.isPresent()) {
            return "redirect:/login";  // Redirigir si el usuario no se encuentra
        }

        boolean showProfileSection = filter == null;
        model.addAttribute("showProfileSection", showProfileSection);
        
        User user = optionalUser.get();
        
        model.addAttribute("username", user.getName());
        model.addAttribute("profilePic", "");
        model.addAttribute("location", "");
        model.addAttribute("salesNumber", "");
        model.addAttribute("purchasesNumber", "");
        model.addAttribute("reviewsNumber", "");

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
}