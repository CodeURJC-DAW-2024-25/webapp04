package es.grupo04.backend.controller;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.core.io.Resource;

import es.grupo04.backend.model.Image;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.service.ProductService;
import es.grupo04.backend.service.ImageService;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        Optional<Product> productOptional = productService.findById(id);
        
        if (productOptional.isEmpty()) {
            model.addAttribute("message", "Producto no encontrado"); // Pasar un mensaje de error
            return "error"; // Página de error si no se encuentra el producto
        }

        model.addAttribute("product", productOptional.get());
        //imagenes del producto
        ArrayList<String> imageURLs = new ArrayList<>();
        for(Image image : productOptional.get().getImages()){
            imageURLs.add("/product/image/"+image.getId());
        }
        model.addAttribute("images",imageURLs);
        

        return "productDetail_template";
    }

    @GetMapping("/product/image/{id}")
    public ResponseEntity<Object> getProductImage(@PathVariable Long id, Model model) throws SQLException {
        Optional<Image> imageOptional = imageService.findById(id);
        
        if (imageOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Blob image = imageOptional.get().getImage();
        Resource file = new InputStreamResource(image.getBinaryStream());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .contentLength(image.length()).body(file);

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
