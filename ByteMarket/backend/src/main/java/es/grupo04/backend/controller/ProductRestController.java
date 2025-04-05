package es.grupo04.backend.controller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.grupo04.backend.dto.NewProductDTO;
import es.grupo04.backend.dto.ProductDTO;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.service.ProductService;
import es.grupo04.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Operation (summary= "Retrieve a list of products")
    @GetMapping
    public Page<ProductDTO> getAllProducts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size, @RequestParam(required = false) String name,
            @RequestParam(required = false) String category, @RequestParam(required = false) boolean recommended,
            @RequestParam(required = false) boolean available) {
        
        Page<ProductDTO> productsPage;
        if(recommended){
            List<ProductDTO> topRated = productService.findTopRatedSellersProducts();
            size = 9;
            return new PageImpl<>(topRated, PageRequest.of(page, size), topRated.size());
        } else{
            if (category != null && !category.isEmpty()) {
                productsPage = productService.findAvailableByCategory(category, page, size);
            } else {
                if (available) {
                    productsPage = productService.findPaginatedAvailable(page, size);
                } else {
                    productsPage = productService.findPaginated(PageRequest.of(page, size));
                }
            }
            if (name != null && !name.isEmpty()) {
                List<ProductDTO> filteredByName = productService.findAll().stream()
                    .filter(product -> product.name().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
                    return new PageImpl<>(filteredByName, PageRequest.of(page, size), filteredByName.size());
            }
        }
        return productsPage;
    }
    
    @Operation (summary= "Retrieve a product by its ID")
    @GetMapping("/{id}")
    public Optional<ProductDTO> getProductById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @Operation (summary= "Create a new product")
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody NewProductDTO productDTO,
            HttpServletRequest request) throws IOException {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserBasicDTO userDTO = userService.findByMail(principal.getName()).get();

        ProductDTO newProduct = productService.save(productDTO, userDTO.id());
        if (newProduct == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(newProduct.id()).toUri();
        return ResponseEntity.created(location).body(newProduct);
    }

    @Operation (summary= "Update product by its ID")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody NewProductDTO newProductDTO,
            HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<ProductDTO> productOpt = productService.findById(id);
        if (productOpt.isEmpty()) {
            throw new NoSuchElementException();
        }

        ProductDTO productDTO = productOpt.get();
        UserBasicDTO userDTO = userService.findByMail(principal.getName()).get();
        if (productDTO.owner().id() != userDTO.id()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<ProductDTO> editedProduct = productService.updateProduct(productDTO, newProductDTO);
        if (!editedProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(editedProduct.get());
    }

    @Operation (summary= "Delete product by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<ProductDTO> productOpt = productService.findById(id);
        if (productOpt.isEmpty()) {
            throw new NoSuchElementException();
        }

        ProductDTO productDTO = productOpt.get();
        UserBasicDTO userDTO = userService.findByMail(principal.getName()).get();
        if (productDTO.owner().id() != userDTO.id()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        productService.delete(id);
        return ResponseEntity.ok(productDTO);
    }

    @Operation (summary= "Add image to a product by product ID")
    @PostMapping("/{id}/images")
    public ResponseEntity<Void> addImage(@PathVariable long id, @RequestParam MultipartFile image,
            HttpServletRequest request) throws IOException {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserBasicDTO userDTO = userService.findByMail(principal.getName()).get();
        Optional<ProductDTO> optionalProduct = productService.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new NoSuchElementException();
        }

        ProductDTO productDTO = optionalProduct.get();
        if (productDTO.owner().id() != userDTO.id()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if(productDTO.imageUrls().size() >= 5){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        productService.addImageEditing(productDTO, image);
        return ResponseEntity.ok().build();

    }

    @Operation (summary= "Delete image of a product by product ID and image ID")
    @DeleteMapping("/{productId}/images/{imageId}")
    public ResponseEntity<Void> removeImage(@PathVariable long productId, @PathVariable long imageId,
            HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserBasicDTO userDTO = userService.findByMail(principal.getName()).get();
        Optional<ProductDTO> optionalProduct = productService.findById(productId);
        if (optionalProduct.isEmpty()) {
            throw new NoSuchElementException();
        }

        ProductDTO productDTO = optionalProduct.get();
        if (productDTO.owner().id() != userDTO.id()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (!productService.imageBelongsToProduct(productId, imageId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(productDTO.imageUrls().size() <= 1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        productService.removeImage(productId, imageId);
        return ResponseEntity.ok().build();

    }

}
