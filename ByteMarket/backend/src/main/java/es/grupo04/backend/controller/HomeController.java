package es.grupo04.backend.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

import es.grupo04.backend.model.Product;
import es.grupo04.backend.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

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
        // Show 8 products per page
        int pageSize = 8;
        List<Product> products = productService.findPaginated(page, pageSize);
        model.addAttribute("other_products", products);
        model.addAttribute("currentPage", page);
        return "home_template";
    }
}