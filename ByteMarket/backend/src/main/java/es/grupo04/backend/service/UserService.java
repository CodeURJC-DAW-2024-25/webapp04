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
        if (!validateName(user.getName())) { // Check if name is valid
            return false;
        }

        if(!validateMail(user)) { // Check if mail is valid
            return false;
        }

        if(!validatePassword(user, confirmPassword)) { // Check if password is valid
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

    public Optional<String> editProfile(User user, User oldUser, String password, String repeatPassword) {
        if(!user.getName().equals(oldUser.getName())){
            if(!validateName(user.getName())) {
                return Optional.of("Nombre de usuario no válido");
            }
            oldUser.setName(user.getName());
        }

        if(password.length()>0 && repeatPassword.length()>0) {
            if(!password.equals(repeatPassword)){
                return Optional.of("Las contraseñas no coinciden");
            }
            oldUser.setEncodedPassword(passwordEncoder.encode(password));
        }

        repository.save(oldUser);
        return Optional.empty();
    
    }
    
    //Is Owner
    public boolean isOwner(User user, Product product) {
        return user.equals(product.getOwner());
    }
    //Is Favorite
    public boolean isFavorite(User user, Product product) {
        return user.getFavoriteProducts().contains(product);
    }


    private boolean validateName(String name) {
        if (name == null || name.isEmpty() || name.length() > 16) { 
            return false;
        }
        return true; 
    }

    private boolean validatePassword(User user, String password) {
        if (user.getEncodedPassword() == null || user.getEncodedPassword().isEmpty() 
        || password == null || password.isEmpty() || !user.getEncodedPassword().equals(password)) { 
            return false;
        }
        return true; 
    }

    private boolean validateMail(User user) {
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"; // Email regex
        if (user.getMail() == null || user.getMail().isEmpty() || !Pattern.matches(emailRegex, user.getMail())) {
            return false;
        }
        return true;
    }
}
