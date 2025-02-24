package es.grupo04.backend.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.grupo04.backend.model.Product;
import es.grupo04.backend.repository.ProductRepository;
import es.grupo04.backend.service.ProductService;

public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable("id") Long id, Model model) {
        Optional<Product> productOptional = productService.findById(id);
        
        if (productOptional.isEmpty()) {
            model.addAttribute("message", "Producto no encontrado"); // Pasar un mensaje de error
            return "error"; // Página de error si no se encuentra el producto
        }
        
        model.addAttribute("product", productOptional.get());
        return "productDetail_template";
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
        model.addAttribute("featured_products", productRepository.findAll());
        model.addAttribute("carrusel", true);
        model.addAttribute("featured_title", "Productos Destacados");
        model.addAttribute("other_products_title", "Otros Productos");

        return "home_template";
    }

    @GetMapping("/removeProduct/{id}")
	public String removeProduct(Model model, @PathVariable long id) {

		Optional<Product> productOptional = productService.findById(id);
		if (productOptional.isPresent()) {
			productService.delete(id);
			model.addAttribute("productOptional", productOptional.get());
		}
		return "deletedProduct";
	}

    @GetMapping("/newProduct")
	public String newBook(Model model) {

		// TODO haría falta poner algo?

		return "newProduct";
	}

}
