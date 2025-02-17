package es.grupo04.controller;
/*
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ch.qos.logback.core.model.Model;
import java.util.Optional;

import es.grupo04.model.Product;
import es.grupo04.service.ProductService;

@Controller
public class ProductWebController {
    @GetMapping({"/"})
    public String showProducts(Model model) {
      model.addAttribute("products", this.ProductService.findAll());
      return "products";
    }

    @GetMapping({"/products/{id}"})
    public String showBook(Model model, @PathVariable long id) {
      Optional<Product> product = this.ProductService.findById(id);
      if (product.isPresent()) {
         model.addAttribute("product", product.get());
         return "product";
      } else {
         return "products";
      }
    }
}*/
