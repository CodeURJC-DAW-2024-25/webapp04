package es.grupo04.backend.service;

import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialBlob;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.grupo04.backend.dto.EditUserDTO;
import es.grupo04.backend.dto.NewUserDTO;
import es.grupo04.backend.dto.ProductBasicDTO;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.dto.UserBasicMapper;
import es.grupo04.backend.dto.UserDTO;
import es.grupo04.backend.dto.UserMapper;
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

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserBasicMapper userBasicMapper;

    @Autowired
    private UserMapper userMapper;

    public List<UserBasicDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userBasicMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserBasicDTO> findByMail(String mail) {
        return userRepository.findByMail(mail).map(userBasicMapper::toDTO);
    }

    public Optional<UserBasicDTO> findById(Long id) {
        return userRepository.findById(id).map(userBasicMapper::toDTO);
    }

    public Optional<UserBasicDTO> findByName(String name) {
        return userRepository.findByName(name).map(userBasicMapper::toDTO);
    }

    public boolean createAccount(NewUserDTO userDTO) {
        if (userRepository.findByMail(userDTO.mail()).isPresent()) {
            return true;
        } else {
            User user = new User(userDTO.mail(), userDTO.name(), userDTO.password());
            user.setRoles(List.of("USER"));

            String encodedPassword = passwordEncoder.encode(user.getEncodedPassword());
            user.setEncodedPassword(encodedPassword);
            userRepository.save(user);
            return false;
        }
    }

    // User Validation
    public boolean validateUser(NewUserDTO user) {
        if (!validateName(user.name())) { // Check if name is valid
            return false;
        }

        if (!validateMail(user)) { // Check if mail is valid
            return false;
        }

        if (!validatePassword(user)) { // Check if password is valid
            return false;
        }

        return true; // If all checks pass, return true
    }

    // Add to favorites
    public boolean addToFavorite(Long productId, UserBasicDTO userDTO) {
        Optional<Product> productOptional = productService.findById(productId);
        Optional<User> userOptional = userRepository.findById(userDTO.id());
        // Check if the product exists
        if (productOptional.isPresent() && userOptional.isPresent()) {
            Product product = productOptional.get();
            User user = userOptional.get();
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
            } else {
                System.out.println("El producto ya está en los favoritos.");
                return false;
            }
        }
        return false;
    }

    public boolean removeFromFavorite(Long productId, UserBasicDTO userDTO) {
        Optional<Product> productOptional = productService.findById(productId);
        Optional<User> userOptional = userRepository.findById(userDTO.id());

        if (productOptional.isPresent() && userOptional.isPresent()) {
            User user = userOptional.get();
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

    public Optional<String> editProfile(EditUserDTO editUserDTO) {
        // Get the user from the repository
        Optional<User> optionalUser = userRepository.findById(editUserDTO.id());
        if (!optionalUser.isPresent()) {
            return Optional.of("Usuario no encontrado");
        }
        User user = optionalUser.get();

        // Name update
        if (editUserDTO.name() != null && !editUserDTO.name().equals(user.getName())) {
            if (!validateName(editUserDTO.name())) {
                return Optional.of("Nombre de usuario no válido");
            }
            user.setName(editUserDTO.name());
        }

        // Password update
        if (editUserDTO.password() != null && !editUserDTO.password().isEmpty() &&
                editUserDTO.repeatPassword() != null && !editUserDTO.repeatPassword().isEmpty()) {

            if (!editUserDTO.password().equals(editUserDTO.repeatPassword())) {
                return Optional.of("Las contraseñas no coinciden");
            }
            user.setEncodedPassword(passwordEncoder.encode(editUserDTO.password()));
        }

        // Address update
        if (editUserDTO.iframe() != null && !editUserDTO.iframe().isEmpty()) {
            user.setIframe(editUserDTO.iframe());
        }

        // Image update
        if (editUserDTO.imageFile() != null && editUserDTO.imageFile().length > 0) {
            try {
                Blob imageBlob = new SerialBlob(editUserDTO.imageFile());
                user.setImageFile(imageBlob);
                user.setImage(true);
            } catch (Exception e) {
                return Optional.of("Error al procesar la imagen");
            }
        } else if (editUserDTO.image()) {
            user.setImage(false);
            user.setImageFile(null);
        }

        userRepository.save(user);
        return Optional.empty();
    }

    // Is Owner
    private boolean isOwner(User user, Product product) {
        return user.equals(product.getOwner());
    }

    // Is Favorite
    public boolean isFavorite(UserBasicDTO user, ProductBasicDTO product) {
        Long userId = user.id();
        Long productId = product.id();
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Product> productOptional = productService.findById(productId);
        if (userOptional.isEmpty() || productOptional.isEmpty()) {
            return false;
        } else {
            return userOptional.get().getFavoriteProducts().contains(productOptional.get());
        }
    }

    private boolean validateName(String name) {
        if (name == null || name.isEmpty() || name.length() > 16) {
            return false;
        }
        return true;
    }

    // Form Validations
    private boolean validatePassword(NewUserDTO user) {
        if (user.password() == null || user.password().isEmpty()
                || user.repeatPassword() == null || user.repeatPassword().isEmpty()
                || !user.password().equals(user.repeatPassword())) {
            return false;
        }
        return true;
    }

    private boolean validateMail(NewUserDTO user) {
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"; // Email regex
        if (user.mail() == null || user.mail().isEmpty() || !Pattern.matches(emailRegex, user.mail())) {
            return false;
        }
        return true;
    }

    public void saveProfilePic(User user, MultipartFile profilePic) throws IOException {

        Blob blob = BlobProxy.generateProxy(profilePic.getInputStream(), profilePic.getSize());

        user.setImageFile(blob);
        user.setImage(true);
    }

    public void delete(UserBasicDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(userDTO.id());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<User> deleteUserOptional = userRepository.findByName("Usuario Eliminado");
            if (deleteUserOptional.isPresent()) {
                User deleteUser = deleteUserOptional.get();
                // Remove user from purchases
                if (user.getPurchases() != null) {
                    for (Purchase purchase : user.getPurchases()) {
                        purchase.setBuyer(deleteUser);
                        purchaseService.save(purchase);
                    }
                }
                if (user.getSales() != null) {
                    for (Purchase sale : user.getSales()) {
                        sale.setSeller(deleteUser);
                        Product product = sale.getProduct();
                        product.setOwner(deleteUser);
                        productService.save(product);
                        purchaseService.save(sale);
                    }
                }

                productService.deleteFavorites(user.getProducts());
                reportService.deleteAllReportsByUser(user);
                userRepository.delete(user);
                userRepository.save(deleteUser);
            }
        }
    }

    public List<ChartData> getStats(UserBasicDTO userDto) {
        List<ChartData> stats = new ArrayList<>();
        HashMap<Integer, ChartData> purchases = new HashMap<>();
        HashMap<Integer, ChartData> sales = new HashMap<>();

        Optional<User> userOptional = userRepository.findById(userDto.id());
        if (userOptional.isEmpty()) {
            return stats;
        }
        User user = userOptional.get();

        for (Purchase purchase : user.getPurchases()) {
            int month = purchase.getPurchaseDate().getMonthValue();
            if (purchases.containsKey(month)) {
                purchases.get(month).addOne();
            } else {
                purchases.put(month, new ChartData(month, "purchase", 1));
            }
        }
        stats.addAll(purchases.values());

        for (Purchase sale : user.getSales()) {
            int month = sale.getPurchaseDate().getMonthValue();
            if (sales.containsKey(month)) {
                sales.get(month).addOne();
            } else {
                sales.put(month, new ChartData(month, "sale", 1));
            }
        }
        stats.addAll(sales.values());

        return stats;
    }

    public boolean hasBought(UserBasicDTO user, UserBasicDTO owner) {
        Optional<User> userOptional = userRepository.findById(user.id());
        Optional<User> ownerOptional = userRepository.findById(owner.id());
        if(!userOptional.isPresent() || !ownerOptional.isPresent()) {
            return false;
        }else{
            return purchaseService.hasBought(userOptional.get(), ownerOptional.get());
        }

    }
}