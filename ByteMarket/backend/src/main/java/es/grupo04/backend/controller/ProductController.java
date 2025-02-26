package es.grupo04.backend.controller;

import java.io.IOException;
import java.security.Principal;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import es.grupo04.backend.model.Image;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.User;
import es.grupo04.backend.service.ProductService;
import es.grupo04.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import es.grupo04.backend.service.ImageService;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;

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
    public String newProduct(Model model) {
        model.addAttribute("product", new Product()); // Crear un nuevo objeto Producto
        return "newProduct"; // Devolver la vista del formulario de creación
    }

    @PostMapping("/newProduct")
    public String createProduct(@ModelAttribute Product product, @RequestParam("imageUpload") MultipartFile[] images, Model model, HttpServletRequest request) throws IOException {
        // Establecer el propietario del producto, suponiendo que el usuario es un "User" en el sistema
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            String username = principal.getName();
            // Obtener el objeto `User` correspondiente al usuario logueado, posiblemente con un servicio de usuarios
            Optional<User> user = userService.findByName(username);
            
            if (user.isPresent()) {
                product.setOwner(user.get()); // Establecer el propietario del producto
            }
        } else {
            model.addAttribute("message", "Usuario no identificado");
            return "error";
        }

        productService.addImages(product, images); // Añadir las imágenes al producto
        
        productService.save(product); // Guardar el nuevo producto en la base de datos

        model.addAttribute("productId", product.getId()); // Añadir el id del producto a los atributos del modelo
        return "redirect:/product/" + product.getId(); // Redirigir a la página de detalles del producto recién creado
    }

}
