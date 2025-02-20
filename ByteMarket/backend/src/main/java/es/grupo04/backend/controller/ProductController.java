package es.grupo04.backend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.grupo04.backend.model.Product;
import es.grupo04.backend.repository.ProductRepository;

public class ProductController {

    @Autowired
    private ProductRepository ProductService;


    public ProductController() {
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Optional<Product> product = ProductService.findById(id);

        if (!product.isPresent()) {
            //error
        }

        model.addAttribute("product", product.get());

        return "product";
    }

}
