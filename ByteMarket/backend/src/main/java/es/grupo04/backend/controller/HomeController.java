package es.grupo04.backend.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

import es.grupo04.backend.model.Product;
import es.grupo04.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
public class HomeController {

    @Autowired
    private ProductRepository productRepository;

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
    public String home(Model model) {

        //TODO hay que quitar esto y meterlo en el service
        //El profe me ha dicho que esto esta mal lo de las categorias
        // Obtener todas las categorías únicas de los productos
        List<String> categories = productRepository.findAll()
                .stream()
                .map(Product::getCategory)
                .distinct()
                .collect(Collectors.toList());
        
        model.addAttribute("navbar_home", "Inicio");
        model.addAttribute("categories", categories);
        model.addAttribute("other_products", productRepository.findAll());

        model.addAttribute("featured_title", "Productos Destacados");
        model.addAttribute("other_products_title", "Otros Productos");

        return "home_template";
    }
    
}