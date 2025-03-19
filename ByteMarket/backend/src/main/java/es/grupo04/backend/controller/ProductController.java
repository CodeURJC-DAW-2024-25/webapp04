package es.grupo04.backend.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.grupo04.backend.dto.NewProductDTO;
import es.grupo04.backend.dto.NewReportDTO;
import es.grupo04.backend.dto.ProductDTO;
import es.grupo04.backend.dto.ReportDTO;
import es.grupo04.backend.model.Image;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.Review;
import es.grupo04.backend.model.User;
import es.grupo04.backend.service.ImageService;
import es.grupo04.backend.service.ProductService;
import es.grupo04.backend.service.PurchaseService;
import es.grupo04.backend.service.ReportService;
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

    @Autowired
    private ReportService reportService;

    @Autowired
    private PurchaseService purchaseService;

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
        Optional<ProductDTO> productOptional = productService.findById(id);

        if (productOptional.isEmpty()) {
            model.addAttribute("message", "Producto no encontrado"); 
            return "error"; 
        }
        ProductDTO product = productOptional.get();
        model.addAttribute("product", product);

        Principal principal = request.getUserPrincipal();
        boolean isOwner = false;
        boolean isFavorite = false;
        boolean hasBought = false;
        boolean hasBoughtProduct = false;
        boolean deletedUser = false;

        if (principal != null) {
            Optional<User> userOptional = userService.findByMail(principal.getName());
            if (userOptional.isPresent()) {
                isOwner = userService.isOwner(userOptional.get(), product);
                isFavorite = userService.isFavorite(userOptional.get(), product);
                hasBought = userService.hasBought(userOptional.get(), product.getOwner());
                hasBoughtProduct = purchaseService.hasUserBoughtProduct(userOptional.get(), product);
                deletedUser = (product.getOwner().getId() == 1);    //Deleted Users Info management
            }
        }

        if (product.getOwner().getIframe() != null){
            model.addAttribute("location", product.getOwner().getIframe());
        }
        
        model.addAttribute("deletedUser", deletedUser);
        model.addAttribute("hasBought", hasBought);
        model.addAttribute("hasBoughtProduct", hasBoughtProduct);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("isFavorite", isFavorite);
        model.addAttribute("product", productOptional.get());
        model.addAttribute("salesNumber", productOptional.get().getOwner().getSales().size());
        model.addAttribute("purchasesNumber", productOptional.get().getOwner().getPurchases().size());
        model.addAttribute("reviewsNumber", productOptional.get().getOwner().getReviews().size());
        model.addAttribute("rating", productService.calculateRating(productOptional.get().getOwner()));
        

        // Product images
        ArrayList<String> imageURLs = new ArrayList<>();
        for (String imageURL : productOptional.get().imageUrls()) {
            imageURLs.add(imageURL);
        }
        model.addAttribute("images", imageURLs);


        List<Review> reviews = product.getOwner().getReviews();
        if (reviews == null || reviews.isEmpty()) {
            model.addAttribute("reviewsSection", false); 
        } else {
            model.addAttribute("reviewsSection", true);
        }

        List<Map<String, Object>> reviewStars = new ArrayList<>();

        for (Review review : reviews) {
            int rating = review.getRating();
            List<Boolean> stars = new ArrayList<>();
            List<Boolean> emptyStars = new ArrayList<>();
            
            // Stars full
            for (int i = 0; i < rating; i++) {
                stars.add(true);
            }

            // Stars empty
            for (int i = rating; i < 5; i++) {
                emptyStars.add(false);
            }

            Map<String, Object> reviewStarData = new HashMap<>();
            reviewStarData.put("rating", rating);
            reviewStarData.put("stars", stars); // Full stars list
            reviewStarData.put("emptyStars", emptyStars); // Empty stars list
            reviewStarData.put("owner", review.getreviewOwner().getName());
            reviewStarData.put("description", review.getDescription());
            reviewStars.add(reviewStarData);
        }

        model.addAttribute("reviewStars", reviewStars);

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
        Principal principal = request.getUserPrincipal();
        // Check if the user is logged in
        if (principal != null) {
            String username = principal.getName();
            Optional<User> userOptional = userService.findByMail(username);
            Optional<ProductDTO> productOptional = productService.findById(id);

            // Check if the user and the product exist
            if (userOptional.isPresent() && productOptional.isPresent()) {
                if (add) {
                    userService.addToFavorite(id, userOptional.get());
                } else {
                    userService.removeFromFavorite(id, userOptional.get());
                }
                return "redirect:/product/" + id; 
            }
        } else {
            return "redirect:/login";
        }
        model.addAttribute("message", add ? "No se pudo añadir a favoritos" : "No se pudo eliminar de favoritos");
        return "error";
    }

    // New Report
    @PostMapping("/product/{id}/newReport")
    public String addReport(@PathVariable Long id, @RequestParam("reason") String reason, @RequestParam("description") String description, HttpServletRequest request, Model model) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            Optional<User> userOptional = userService.findByMail(principal.getName());
            Optional<ProductDTO> productOptional = productService.findById(id);

            if (userOptional.isPresent() && productOptional.isPresent()) {
                Long userId = userOptional.get().getId();
                Long productId = productOptional.get().id();
    
                NewReportDTO newReportDTO = new NewReportDTO(reason, description, productId, userId);
                reportService.saveReport(newReportDTO);
    
                return "redirect:/product/" + id;
            }
        }

        model.addAttribute("message", "No se pudo enviar el reporte");
        return "error";
    }

    // Show the reports
    @GetMapping("/reports")
    public String getReports(Model model) {
        List<ReportDTO> reports = reportService.getAllReports();
        model.addAttribute("reports", reports);
        model.addAttribute("title", "Reportes");
        return "reports";
    }

    @PostMapping("/solve-report/{id}")
    public String solveReport(@PathVariable Long id, Model model) {
        Optional<ReportDTO> reportOptional = reportService.findById(id);
        if (reportOptional.isEmpty()) {
            model.addAttribute("message", "Reporte no encontrado");
            return "error";
        }
        reportService.delete(id);
        return "redirect:/reports";
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
    public String removeProduct(@PathVariable long id) {

        Optional<ProductDTO> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            productService.delete(id);
        }
        return "redirect:/"; 
    }
    // Edit Product
    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        Optional<ProductDTO> optionalProduct = productService.findById(id);
    
        if (!optionalProduct.isPresent()) {
            model.addAttribute("message", "Producto no encontrado");
            return "error";
        }
    
        ProductDTO product = optionalProduct.get();
        System.out.println("El producto que se pasa es:" + product.name());
        Optional<User> optionalUser = userService.findByMail(userDetails.getUsername());
    
        if (!optionalUser.isPresent() || !product.getOwner().equals(optionalUser.get())) {
            model.addAttribute("message", "No autorizado para editar este producto");
            return "error";
        }
        
        model.addAttribute("product", product);
        model.addAttribute("edit", true); 
        model.addAttribute("images", product.imageUrls());
        model.addAttribute("oneImage", product.imageUrls().size() == 1);
        model.addAttribute("maxImages", product.imageUrls().size() == 5);

        return "newProduct";
    }

    @PostMapping("/editProduct/{id}")
    public String updateProduct(@PathVariable long id, @ModelAttribute NewProductDTO product, Model model) {
        Optional<ProductDTO> optionalProduct = productService.findById(id);
        if (!optionalProduct.isPresent()) {
            model.addAttribute("message", "Producto no encontrado");
            return "error";
        }
        ProductDTO oldProduct = optionalProduct.get();
        productService.updateProduct(oldProduct, product);
        return "redirect:/product/" + id;
    }
     

    @PostMapping("/delete/image/{id}")
    public String removeImage(@PathVariable long id, @RequestParam("productId") long productId, Model model) {
        Optional<ProductDTO> productOptional = productService.findById(productId);
        if(productOptional.get().imageUrls().size() == 1){
            model.addAttribute("message", "Un producto debe tener al menos una imagen");
            return "error";
        }

        if (productOptional.isPresent()) {
            productService.removeImage(productId, id);
        }
        return "redirect:/editProduct/" + productId;
    }

    @PostMapping("/add/image/{id}")
    public String addImage(@PathVariable long id, @RequestParam("imageUpload") MultipartFile image, Model model) throws IOException {
        Optional<ProductDTO> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            ProductDTO product = productOptional.get();
            productService.addImageEditing(product, image);
        } else {
            model.addAttribute("message", "Producto no encontrado");
            return "error";
        }
        return "redirect:/editProduct/" + id;
    }

    @GetMapping("/newProduct")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        return "newProduct"; 
    }

    @PostMapping("/newProduct")
    public String createProduct(@ModelAttribute NewProductDTO product,
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


        if(product.imageUpload().length > 5){
            model.addAttribute("message", "No puedes subir más de 5 imágenes");
            return "error";
        }

        ProductDTO savedProduct = productService.save(product);

        model.addAttribute("productId", savedProduct.id()); // add id of the product as attribute
        return "redirect:/product/" + savedProduct.id(); // redirect to the detail screen product created as new
    }

    // To show by category or to search in header
    @GetMapping("/products")
    public String getProducts(
        @RequestParam(name = "category", required = false) String category,
        @RequestParam(name = "search", required = false) String search,
        @RequestParam(value = "page", defaultValue = "0") int page,
        Model model) {

        int pageSize = 8;
        Page<ProductDTO> productPage;

        if (search != null && !search.isEmpty()) {
            productPage = productService.searchByName(search, page, pageSize);
        } else if (category != null && !category.isEmpty()) {
            productPage = productService.findAvailableByCategory(category, page, pageSize);
        } else {
            productPage = productService.findPaginatedAvailable(page, pageSize);
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
