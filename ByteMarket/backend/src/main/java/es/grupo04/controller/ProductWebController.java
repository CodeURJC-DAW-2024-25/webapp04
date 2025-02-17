package es.grupo04.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import es.grupo04.model.Product;
import es.grupo04.service.ProductService;
import es.grupo04.service.ShopService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ProductWebController {

    @Autowired
	  private ProductService productService;

    @Autowired
	  private ShopService shopService;

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

    @GetMapping({"/"})
    public String showProducts(Model model) {
      model.addAttribute("products", this.productService.findAll());
      return "products";
    }

    @GetMapping({"/products/{id}"})
    public String showBook(Model model, @PathVariable long id) {
      Optional<Product> product = this.productService.findById(id);
      if (product.isPresent()) {
         model.addAttribute("product", product.get());
         return "product";
      } else {
         return "products";
      }
    }
}
