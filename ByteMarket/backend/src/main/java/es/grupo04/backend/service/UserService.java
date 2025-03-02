package es.grupo04.backend.service;


import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.Purchase;
import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        System.out.println("Saving user");
        if (user.getRoles() == null) {
            user.setRoles(List.of("USER")); // Asignar rol por defecto si no se pasa en el formulario
        }
        String encodedPassword = passwordEncoder.encode(user.getEncodedPassword());
        user.setEncodedPassword(encodedPassword);
        return userRepository.save(user);
    }

    public Optional<User> findByMail(String mail) {
        return userRepository.findByMail(mail);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public boolean createAccount(User user) {
        if (userRepository.findByMail(user.getMail()).isPresent()) {
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
                userRepository.save(user);
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
                userRepository.save(user);
                System.out.println("Producto eliminado de favoritos: " + product.getName());
                return true;
            } else {
                System.out.println("El producto no está en los favoritos.");
                return false;
            }
        }
        return false;
    }

    public Optional<String> editProfile(User user, User oldUser, String password, String repeatPassword, String iframe) {
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

        if(iframe != null && !iframe.isEmpty()) {
            oldUser.setIframe(iframe);
        }

        userRepository.save(oldUser);
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

    public void saveProfilePic(User user, MultipartFile profilePic) throws IOException {

        Blob blob = BlobProxy.generateProxy(profilePic.getInputStream(),profilePic.getSize());

		user.setImageFile(blob);
        user.setImage(true);
    }

    public void delete(User user) {

        // Remove user from purchases
        if (user.getPurchases() != null) {
            user.getPurchases().forEach(purchase -> purchase.setBuyer(null));
            user.setPurchases(null);
        }

        // Remove user from sales
        if (user.getSales() != null) {
            user.getSales().forEach(sale -> sale.setSeller(null));
            user.setSales(null);
        }

        // Remove user from reviews
        if (user.getReviews() != null) {
            user.getReviews().forEach(review -> review.setreviewOwner(null));
            user.setReviews(null);
        }

        userRepository.delete(user);
    }

    public List<ChartData> getStats(User user) {
        List<ChartData> stats = new ArrayList<>();
        HashMap<Integer, ChartData> purchases = new HashMap<>();
        HashMap<Integer, ChartData> sales = new HashMap<>();

        for(Purchase purchase : user.getPurchases()) {
            int month = purchase.getPurchaseDate().getMonthValue();
            if(purchases.containsKey(month)) {
                purchases.get(month).addOne();
            } else {
                purchases.put(month, new ChartData(month, "purchase", 1));
            }
        }
        stats.addAll(purchases.values());

        for(Purchase sale : user.getSales()) {
            int month = sale.getPurchaseDate().getMonthValue();
            if(sales.containsKey(month)) {
                sales.get(month).addOne();
            } else {
                sales.put(month, new ChartData(month, "sale", 1));
            }
        }
        stats.addAll(sales.values());

        return stats;
    }


}
