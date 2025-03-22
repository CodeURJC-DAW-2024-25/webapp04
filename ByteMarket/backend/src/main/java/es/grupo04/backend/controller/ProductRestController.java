package es.grupo04.backend.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.grupo04.backend.dto.NewProductDTO;
import es.grupo04.backend.dto.ProductDTO;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.dto.UserDTO;
import es.grupo04.backend.service.ProductService;
import es.grupo04.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping
    public Page<ProductDTO> getAllProducts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.findPaginated(pageable);
    }

    @GetMapping("/{id}")
    public Optional<ProductDTO> getProductById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@ModelAttribute NewProductDTO productDTO,
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
        return ResponseEntity.ok(newProduct);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @ModelAttribute NewProductDTO newProductDTO,
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

    @GetMapping("/favorites")
    public ResponseEntity<Page<ProductDTO>> getFavoriteProducts(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserDTO userDTO = userService.findByMailExtendedInfo(principal.getName()).get();

        Page<ProductDTO> productsPage = productService.getFavoriteProducts(userDTO, page, size);
        return ResponseEntity.ok(productsPage);
    }
    
   
    @PostMapping("/{id}/favorites")
    public ResponseEntity<Page<ProductDTO>> toggleFavoriteProduct(HttpServletRequest request,
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (productService.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    
        ProductDTO productDTO = productService.findById(id).get();
        UserDTO userDTO = userService.findByMailExtendedInfo(principal.getName()).get();
    
        if (productDTO.owner().id() == userDTO.id()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        boolean isFavorite = userService.isFavorite(userDTO, id);
        if(isFavorite){
            userService.removeFromFavorite(id, userDTO);
        }else{
            userService.addToFavorite(id, userDTO);
        }

        Page<ProductDTO> productsPage = productService.getFavoriteProducts(userDTO, page, size);
        return ResponseEntity.ok(productsPage);
    }

    @GetMapping("/purchases")
    public ResponseEntity<List<ProductDTO>> getLastPurchases(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserDTO userDTO = userService.findByMailExtendedInfo(principal.getName()).get();

        List<ProductDTO> products = productService.getLastPurchases(userDTO);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/sales")
    public ResponseEntity<List<ProductDTO>> getLastSales(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserDTO userDTO = userService.findByMailExtendedInfo(principal.getName()).get();

        List<ProductDTO> products = productService.getLastPurchases(userDTO);
        return ResponseEntity.ok(products);

    }

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

        productService.addImageEditing(productDTO, image);
        return ResponseEntity.ok().build();

    }

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

        productService.removeImage(productId, imageId);
        return ResponseEntity.ok().build();

    }
}
