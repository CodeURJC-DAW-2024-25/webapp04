package es.grupo04.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User saveUser(User user) {
        System.out.println("Saving user");
        if (user.getRoles() == null) {
            user.setRoles(List.of("USER")); // Asignar rol por defecto si no se pasa en el formulario
        }
        String encodedPassword = passwordEncoder.encode(user.getEncodedPassword());
        user.setEncodedPassword(encodedPassword);
        return repository.save(user);
    }

    public Optional<User> findByMail(String mail) {
        return repository.findByMail(mail);
    }

    public Optional<User> findByName(String name) {
        return repository.findByName(name);
    }

    public boolean createAccount(User user) {
        if (repository.findByMail(user.getMail()).isPresent()) {
            return true; // Si tiene una cuenta devolvemos true
        } else {
            saveUser(user);
            return false;
        }
    }

    // User Validation
    public boolean validateUser(User user, String confirmPassword) {
        if (user.getName() == null || user.getName().isEmpty()) { // Check if name is empty
            return false;
        } else if (user.getName().length() > 16) { // Check if name is longer than 16 characters
            return false;
        }
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"; // Email regex
        if (user.getMail() == null || user.getMail().isEmpty()) { // Check if email is empty
            return false;
        } else if (!Pattern.matches(emailRegex, user.getMail())) { // Check if email is valid
            return false;
        }

        if ((user.getEncodedPassword() == null || user.getEncodedPassword().isEmpty())
                || (confirmPassword == null || confirmPassword.isEmpty())) { // Check if password is empty
            return false;
        } else if (!user.getEncodedPassword().equals(confirmPassword)) { // Check if passwords match
            return false;
        }
        return true; // No errors
    }

    // Add to favorites
    public boolean addToFavorite(Long productId, User user) {
        Optional<Product> productOptional = productService.findById(productId);
        // Check if the product exists
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // Check if the user is the owner of the product
            if (isOwner(user, product)) {
                return false;
            }
            // Get the list of favorite products
            List<Product> favoriteProducts = user.getFavoriteProducts();
            // Check if the product is already in the favorites list
            if (!favoriteProducts.contains(product)) {
                favoriteProducts.add(product);
                user.setFavoriteProducts(favoriteProducts);
                repository.save(user);
                System.out.println("Producto añadido a favoritos: " + product.getName());
                return true;
            } else{
                System.out.println("El producto ya está en los favoritos.");
                return false;
            }
        }
        return false;
    }

    public boolean removeFromFavorite(Long productId, User user) {
        Optional<Product> productOptional = productService.findById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // Get the list of favorite products
            List<Product> favoriteProducts = user.getFavoriteProducts();

            // Check if the product is in the favorites list
            if (favoriteProducts.contains(product)) {
                favoriteProducts.remove(product);
                user.setFavoriteProducts(favoriteProducts);
                repository.save(user);
                System.out.println("Producto eliminado de favoritos: " + product.getName());
                return true;
            } else {
                System.out.println("El producto no está en los favoritos.");
                return false;
            }
        }
        return false;
    }
    
    //Is Owner
    public boolean isOwner(User user, Product product) {
        return user.equals(product.getOwner());
    }
    //Is Favorite
    public boolean isFavorite(User user, Product product) {
        return user.getFavoriteProducts().contains(product);
    }
}
