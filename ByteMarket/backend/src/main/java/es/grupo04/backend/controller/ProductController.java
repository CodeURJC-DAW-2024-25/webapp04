package es.grupo04.backend.controller;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.grupo04.backend.model.Image;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.service.ProductService;

public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        Optional<Product> productOptional = productService.findById(id);
        
        if (productOptional.isEmpty()) {
            model.addAttribute("message", "Producto no encontrado"); // Pasar un mensaje de error
            return "error"; // Página de error si no se encuentra el producto
        }

        model.addAttribute("product", productOptional.get());
        //imagenes del producto
        /*Una forma que quiero intentar es que lo haga directamente 
        ArrayList<Blob> images = new ArrayList<>();
        for(Image image : productOptional.get().getImages()){
            images.add(image.getImage());
        }
        model.addAttribute("images",images);
        */

        /*Otra forma que iria junto con el controlador de /product/image/{id} <- es el id de la imagen
        * asi cada imagen tendria como src /product/image/{id} y pediria al servidor la imagen mediante esa url
        ArrayList<String> imageURLs = new ArrayList<>();
        for(Image image : productOptional.get().getImages()){
            imageURLs.add("/product/image/"+image.getId());
        }
        model.addAttribute("images",imageURLs);
        */

        return "productDetail_template";
    }

    /* Asi podria ser una forma de hacerlo
    @GetMapping("/product/image/{id}")
    public ResponseEntity<Object> getProductImage(@PathVariable Long id, Model model) {
        //devolver la imagen en una respuesta http
    }*/

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
