package es.grupo04.backend.service;

import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.grupo04.backend.dto.EditUserDTO;
import es.grupo04.backend.dto.EditUserMapper;
import es.grupo04.backend.dto.NewUserDTO;
import es.grupo04.backend.dto.ProductDTO;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.dto.UserBasicMapper;
import es.grupo04.backend.dto.UserDTO;
import es.grupo04.backend.dto.UserMapper;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.Purchase;
import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.ProductRepository;
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
    private EditUserMapper edit;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserMapper userMapper;

    public List<UserBasicDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userBasicMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserBasicDTO> findByMail(String mail) {
        return Optional.of(userBasicMapper.toDTO(userRepository.findByMail(mail)
                .orElseThrow(() -> new NoSuchElementException())));
    }

    public Optional<UserDTO> findByMailExtendedInfo(String mail) {
        return Optional.of(userMapper.toDTO(userRepository.findByMail(mail)
                .orElseThrow(() -> new NoSuchElementException())));
    }

    public Optional<EditUserDTO> findByMailEdit(String mail) {
        return Optional.of(edit.toDTO(userRepository.findByMail(mail)
                .orElseThrow(() -> new NoSuchElementException())));
    }

    public Optional<UserBasicDTO> findById(Long id) {
        return Optional.of(userBasicMapper.toDTO(userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException())));
    }

    public Optional<UserDTO> findByIdExtendedInfo(Long id) {
        return Optional.of(userMapper.toDTO(userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException())));
    }

    public Optional<UserBasicDTO> findByName(String name) {
        return Optional.of(userBasicMapper.toDTO(userRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException())));
    }

    public Optional<UserDTO> createAccount(NewUserDTO userDTO) {
        if (userRepository.findByMail(userDTO.mail()).isPresent()) {
            return Optional.empty();
        } else {
            User user = new User(userDTO.name(), userDTO.password(), userDTO.mail());
            user.setRoles(List.of("USER"));
            String encodedPassword = passwordEncoder.encode(userDTO.password());
            user.setEncodedPassword(encodedPassword);
            User savedUser = userRepository.save(user);
            return Optional.of(userMapper.toDTO(savedUser));
        }
    }

    // User Validation
    public boolean validateUser(NewUserDTO user) {
        if (!validateName(user.name())) { // Check if name is valid
            System.out.println("\n\n\nHa pasado: Nombre no válido");
            return false;
        }
        if (!validateMail(user)) { // Check if mail is valid
            System.out.println("\n\n\nHa pasado: Mail no válido");
            return false;
        }
        if (!validatePassword(user)) { // Check if password is valid
            System.out.println("\n\n\nHa pasado: Contraseña no válido");
            return false;
        }
        System.out.println("Contraseña válida");
        return true; // If all checks pass, return true
    }

    // Add to favorites
    public boolean addToFavorite(Long productId, UserBasicDTO userDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException());
        User user = userRepository.findById(userDTO.id())
                .orElseThrow(() -> new NoSuchElementException());

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

    public boolean removeFromFavorite(Long productId, UserBasicDTO userDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException());
        User user = userRepository.findById(userDTO.id())
                .orElseThrow(() -> new NoSuchElementException());

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

    public Optional<String> editProfile(UserBasicDTO oldUser, EditUserDTO editUserDTO) {
        System.out.println("Editando usuario: " + oldUser.id() + " con los datos: " + editUserDTO.profilePicInput());
        System.out.println("Datos de DTO: " + editUserDTO.profilePicInput());
        // Get the user from the repository
        User user = userRepository.findById(oldUser.id())
                .orElseThrow(() -> new NoSuchElementException());

        // Name update
        if (editUserDTO.name() != null && !editUserDTO.name().equals(user.getName())) {
            if (!validateName(editUserDTO.name())) {
                return Optional.of("Nombre de usuario no válido");
            }
            user.setName(editUserDTO.name());
        }

        // Password update
        if (editUserDTO.newPass() != null && !editUserDTO.newPass().isEmpty() &&
                editUserDTO.repeatPass() != null && !editUserDTO.repeatPass().isEmpty()) {

            if (!editUserDTO.newPass().equals(editUserDTO.repeatPass())) {
                return Optional.of("Las contraseñas no coinciden");
            }
            user.setEncodedPassword(passwordEncoder.encode(editUserDTO.newPass()));
        }

        // Address update
        if (editUserDTO.iframe() != null && !editUserDTO.iframe().isEmpty()) {
            user.setIframe(editUserDTO.iframe());
        }
        userRepository.save(user);
        return Optional.empty();
    }

    // Is Owner
    public boolean isOwner(User user, Product product) {
            return user.equals(product.getOwner());
    }

    public boolean isOwner(UserDTO userDTO, ProductDTO productDTO) {
        User user = userRepository.findById(userDTO.id()).get();
        Product product = productRepository.findById(productDTO.id()).get();
        return user.equals(product.getOwner());
    }

    // Is Favorite
    public boolean isFavorite(UserDTO user, Long productId) {
        Long userId = user.id();
        User userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException());
        Product productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException());
        return userEntity.getFavoriteProducts().contains(productEntity);
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
    
    public void saveProfilePic(UserBasicDTO userDTO, MultipartFile profilePic) throws IOException {
        System.out.println("Guardando imagen de perfil para el usuario: " + profilePic.getOriginalFilename());
        User user = userRepository.findById(userDTO.id())
                .orElseThrow(() -> new NoSuchElementException());
        Blob blob = BlobProxy.generateProxy(profilePic.getInputStream(), profilePic.getSize());
        user.setImageFile(blob);
        user.setImage(true);
    }

    public void delete(UserBasicDTO userDTO) {
        User user = userRepository.findById(userDTO.id())
                .orElseThrow(() -> new NoSuchElementException());
        User deleteUser = userRepository.findById(Long.valueOf(1))
                .orElseThrow(() -> new RuntimeException("Usuario Eliminado no encontrado"));

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
                productRepository.save(product);
                productRepository.save(product);
                purchaseService.save(sale);
            }
        }

        productService.deleteFavorites(user.getProducts());
        reportService.deleteAllReportsByUser(user);
        userRepository.delete(user);
        userRepository.save(deleteUser);
    }

    public List<ChartData> getStats(UserBasicDTO userDto) {
        List<ChartData> stats = new ArrayList<>();
        HashMap<Integer, ChartData> purchases = new HashMap<>();
        HashMap<Integer, ChartData> sales = new HashMap<>();

        User user = userRepository.findById(userDto.id())
                .orElseThrow(() -> new NoSuchElementException());

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

    public boolean hasBought(UserDTO user, Long ownerId) {
        UserDTO owner = userMapper.toDTO(userRepository.findById(ownerId).get());
        return purchaseService.hasBought(user, owner);
    }

    public UserDTO getUserDTO(Long id) {
        User user = userRepository.findById(id).get();
        return userMapper.toDTO(user);
    }

    public Blob getImage(UserDTO userDTO) {
        System.out.println("Obteniendo imagen de perfil para el usuario: " + userDTO.id());
        User user = userRepository.findById(userDTO.id()).get();
        return user.getImageFile();
    }

    public Optional<User> findByIdPrueba(Long id) {

            return userRepository.findById(id);
        
    }

    public boolean hasRole(Long userId, String role) {
        return userRepository.findById(userId)
            .map(user -> user.getRoles().contains(role))
            .orElse(false);
    }
}
