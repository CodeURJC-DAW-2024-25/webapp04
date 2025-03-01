package es.grupo04.backend.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

import es.grupo04.backend.model.Image;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.User;
import es.grupo04.backend.service.ImageService;
import es.grupo04.backend.service.ProductService;
import es.grupo04.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

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

            User user = userService.findByMail(principal.getName()).get();

            model.addAttribute("logged", true);
            model.addAttribute("userName", user.getName());    
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
            model.addAttribute("user", user);

        } else {
            model.addAttribute("logged", false);
        }
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable Long id, Model model, HttpServletRequest request) {
        Optional<Product> productOptional = productService.findById(id);

        if (productOptional.isEmpty()) {
            model.addAttribute("message", "Producto no encontrado"); 
            return "error"; 
        }
        Product product = productOptional.get();
        model.addAttribute("product", product);

        Principal principal = request.getUserPrincipal();
        boolean isOwner = false;
        boolean isFavorite = false;

        if (principal != null) {
            Optional<User> userOptional = userService.findByMail(principal.getName());
            if (userOptional.isPresent()) {
                isOwner = userService.isOwner(userOptional.get(), product);
                isFavorite = userService.isFavorite(userOptional.get(), product);
            }
        }

        if (product.getOwner().getIframe() != null){
            model.addAttribute("location", product.getOwner().getIframe());
        }
        
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("isFavorite", isFavorite);
        model.addAttribute("product", productOptional.get());
        model.addAttribute("salesNumber", productOptional.get().getOwner().getSales().size());
        model.addAttribute("purchasesNumber", productOptional.get().getOwner().getPurchases().size());
        model.addAttribute("reviewsNumber", productOptional.get().getOwner().getReviews().size());
        model.addAttribute("rating", productService.calculateRating(productOptional.get().getOwner()));
        

        // Product images
        ArrayList<String> imageURLs = new ArrayList<>();
        for (Image image : productOptional.get().getImages()) {
            imageURLs.add("/product/image/" + image.getId());
        }
        model.addAttribute("images", imageURLs);

        return "productDetail_template";
    }

    // Add to favorites
    @PostMapping("/product/{id}/addFavorite")
    public String addToFavorite(@PathVariable Long id, HttpServletRequest request, Model model) {
        return handleFavoriteAction(id, request, model, true);
    }

    // Remove from favorites
    @PostMapping("/product/{id}/eliminateFavorite")
    public String removeFromFavorite(@PathVariable Long id, HttpServletRequest request, Model model) {
        return handleFavoriteAction(id, request, model, false);
    }

    // Handle favorite action. add = true -> add to favorites, add = false -> remove
    // from favorites
    private String handleFavoriteAction(Long id, HttpServletRequest request, Model model, boolean add) {
        System.out.println(add ? "Añadiendo a favoritos" : "Eliminando de favoritos");
        Principal principal = request.getUserPrincipal();
        // Check if the user is logged in
        if (principal != null) {
            String username = principal.getName();
            Optional<User> userOptional = userService.findByMail(username);
            Optional<Product> productOptional = productService.findById(id);

            // Check if the user and the product exist
            if (userOptional.isPresent() && productOptional.isPresent()) {
                if (add) {
                    userService.addToFavorite(id, userOptional.get());
                } else {
                    userService.removeFromFavorite(id, userOptional.get());
                }
                return "redirect:/product/" + id; // PROVISIONAL: devuelve un token por el redirect --> No es de nuestro
                                                  // formulario
            }
        } else {
            return "redirect:/login";
        }
        model.addAttribute("message", add ? "No se pudo añadir a favoritos" : "No se pudo eliminar de favoritos");
        return "error";
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

    @PostMapping("/delete/{id}")
    public String removeProduct(Model model, @PathVariable long id) {

        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            productService.delete(id);
            model.addAttribute("productOptional", productOptional.get());
        }
        return "redirect:/"; 
    }

    @GetMapping("/newProduct")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        return "newProduct"; 
    }

    @PostMapping("/newProduct")
    public String createProduct(@ModelAttribute Product product, @RequestParam("imageUpload") MultipartFile[] images,
            Model model, HttpServletRequest request) throws IOException {

        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            String username = principal.getName();
            Optional<User> user = userService.findByMail(username);

            if (user.isPresent()) {
                product.setOwner(user.get()); 
            }
        } else {
            model.addAttribute("message", "Usuario no identificado");
            return "error";
        }

        productService.addImages(product, images); 

        productService.save(product); 

        model.addAttribute("productId", product.getId()); // add id of the product as attribute
        return "redirect:/product/" + product.getId(); // redirecta to the detail screen product created as new
    }

    // To show by category or to search in header
    @GetMapping("/products")
    public String getProducts(
        @RequestParam(name = "category", required = false) String category,
        @RequestParam(name = "search", required = false) String search,
        @RequestParam(value = "page", defaultValue = "0") int page,
        Model model) {

        int pageSize = 8;
        Page<Product> productPage;

        if (search != null && !search.isEmpty()) {
            productPage = productService.searchByName(search, page, pageSize);
        } else if (category != null && !category.isEmpty()) {
            productPage = productService.findByCategory(category, page, pageSize);
        } else {
            productPage = productService.findPaginatedCategory(page, pageSize);
        }

        if (productPage.isEmpty()) {
            model.addAttribute("message", "No se encontraron productos.");
            return "error";
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "productByCategory";
}




    


}
