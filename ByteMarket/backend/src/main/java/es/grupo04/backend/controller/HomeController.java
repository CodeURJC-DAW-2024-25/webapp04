package es.grupo04.backend.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import es.grupo04.backend.dto.ProductDTO;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.service.ProductService;
import es.grupo04.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

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
            UserBasicDTO user = userService.findByMail(principal.getName()).get();
            model.addAttribute("logged", true);
            model.addAttribute("userName", user.name());    
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
            model.addAttribute("user", user);

        } else {
            model.addAttribute("logged", false);
        }
    }

    @GetMapping("/")
    public String home(@RequestParam(value = "page", defaultValue = "0") int page, 
                    Model model, 
                    @AuthenticationPrincipal UserDetails userDetails) {
        int pageSize = 8;

        Page<ProductDTO> productPage = productService.findPaginatedAvailable(page, pageSize);
        model.addAttribute("other_products", productPage.getContent());

        List<ProductDTO> topRatedSellersProducts = productService.findTopRatedSellersProducts();
        model.addAttribute("top_seller_products", topRatedSellersProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "home_template";
    }





}