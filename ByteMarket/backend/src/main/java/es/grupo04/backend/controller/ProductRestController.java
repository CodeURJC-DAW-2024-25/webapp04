package es.grupo04.backend.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

import es.grupo04.backend.dto.NewProductDTO;
import es.grupo04.backend.dto.ProductDTO;
import es.grupo04.backend.dto.UserDTO;
import es.grupo04.backend.service.ProductService;
import es.grupo04.backend.service.UserService;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<ProductDTO> getProductById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    public ProductDTO createProduct(@RequestBody NewProductDTO productDTO, @RequestParam Long ownerId) {
        try {
            return productService.save(productDTO, ownerId);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el producto", e);
        }
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO, @RequestBody NewProductDTO newProductDTO) {
        productService.updateProduct(productDTO, newProductDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.delete(id);
    }

    @GetMapping("/favorites")
    public Page<ProductDTO> getFavoriteProducts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Optional<UserDTO> optionalUser = userService.findByMailExtendedInfo(userDetails.getUsername());
        UserDTO userDTO = optionalUser.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return productService.getFavoriteProducts(userDTO, page, size);
    }

    @GetMapping("/purchased")
    public List<ProductDTO> getLastPurchases(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<UserDTO> optionalUser = userService.findByMailExtendedInfo(userDetails.getUsername());
        UserDTO userDTO = optionalUser.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return productService.getLastPurchases(userDTO);
    }

    @GetMapping("/sold")
    public List<ProductDTO> getLastSales(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<UserDTO> optionalUser = userService.findByMailExtendedInfo(userDetails.getUsername());
        UserDTO userDTO = optionalUser.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return productService.getLastSales(userDTO);
    }

    @PostMapping("/{id}/images")
    public void addImage(@PathVariable long id, @RequestParam MultipartFile image) {
        try {
            Optional<ProductDTO> optionalProduct = productService.findById(id);
            ProductDTO productDTO = optionalProduct.orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            productService.addImageEditing(productDTO, image);
        } catch (IOException e) {
            throw new RuntimeException("Error al añadir la imagen", e);
        }
    }

    @DeleteMapping("/{productId}/images/{imageId}")
    public void removeImage(@PathVariable long productId, @PathVariable long imageId) {
        productService.removeImage(productId, imageId);
    }
}
