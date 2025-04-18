package es.grupo04.backend.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo04.backend.dto.ChatDTO;
import es.grupo04.backend.dto.ProductDTO;
import es.grupo04.backend.dto.ProductMapper;
import es.grupo04.backend.dto.PurchaseDTO;
import es.grupo04.backend.dto.PurchaseMapper;
import es.grupo04.backend.dto.UserDTO;
import es.grupo04.backend.dto.UserMapper;
import es.grupo04.backend.model.Chat;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.Purchase;
import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.ChatRepository;
import es.grupo04.backend.repository.ProductRepository;
import es.grupo04.backend.repository.PurchaseRepository;
import es.grupo04.backend.repository.UserRepository;

@Service
public class PurchaseService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;

    public PurchaseDTO save(Purchase purchase) {
        return purchaseMapper.toDTO(purchaseRepository.save(purchase));
    }

    public Optional<PurchaseDTO> findById(Long id) {
        return purchaseRepository.findById(id).map(purchaseMapper::toDTO);
    }

    public List<PurchaseDTO> findAll() {
        return purchaseRepository.findAll()
                .stream()
                .map(purchaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PurchaseDTO> findAllUserPurchases(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
    
        return purchaseRepository.findByBuyerOrSellerOrderByPurchaseDateDesc(user, user)
                .stream()
                .map(purchaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PurchaseDTO createPurchase(ChatDTO chatDTO) {
        Chat chat = chatRepository.findById(chatDTO.id())
                .orElseThrow(() -> new NoSuchElementException());
        User buyer = chat.getUserBuyer();
        User seller = chat.getUserSeller();
        Product product = chat.getProduct();
        
        if (product.isSold()) {
            return null;
        }

        // Actualizar el estado del producto
        product.setSold(true);
        productService.sold(productMapper.toDTO(product), userMapper.toDTO(buyer));
        productRepository.saveAndFlush(product);

        // Creación de la compra
        Purchase purchase = new Purchase(product, buyer, seller);        
        purchaseRepository.saveAndFlush(purchase);

        // Update User
        buyer.addPurchase(purchase);
        seller.addSale(purchase);
        userRepository.save(buyer);
        userRepository.save(seller);

        return purchaseMapper.toDTO(purchase);
    }

    public List<PurchaseDTO> findByBuyer(UserDTO buyerDTO) {
        User buyer = userRepository.findById(buyerDTO.id())
                .orElseThrow(() -> new NoSuchElementException());
        return purchaseRepository.findByBuyerOrderByPurchaseDateDesc(buyer)
                .stream()
                .map(purchaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PurchaseDTO> findByBuyerSeller(Long buyerId, Long sellerId) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new NoSuchElementException());
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new NoSuchElementException());
        return purchaseRepository.findByBuyerAndSeller(buyer, seller)
                .stream()
                .map(purchaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PurchaseDTO> getLastSales(Long sellerId) {
        User user = userRepository.findById(sellerId).get();
        return purchaseRepository.findBySellerOrderByPurchaseDateDesc(user)
                .stream()
                .limit(8)
                .map(purchaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PurchaseDTO> getLastPurchases(Long buyerId) {
        User user = userRepository.findById(buyerId).get();
        return purchaseRepository.findByBuyerOrderByPurchaseDateDesc(user)
                .stream()
                .limit(8)
                .map(purchaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean hasBought(UserDTO user2, UserDTO owner2) {
        User user = userRepository.findById(user2.id())
                .orElseThrow(() -> new NoSuchElementException());
        User owner = userRepository.findById(owner2.id())
                .orElseThrow(() -> new NoSuchElementException());
        return !purchaseRepository.findByBuyerAndSeller(user, owner).isEmpty();
    }

    public boolean hasBought(Long buyerId, Long sellerId) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new NoSuchElementException());
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new NoSuchElementException());
        return !purchaseRepository.findByBuyerAndSeller(buyer, seller).isEmpty();
    }

    public boolean hasUserBoughtProduct(UserDTO userDTO, ProductDTO productDTO) {
        User user = userRepository.findById(userDTO.id())
                .orElseThrow(() -> new NoSuchElementException());
        Product product = productRepository.findById(productDTO.id())
                .orElseThrow(() -> new NoSuchElementException());
        return purchaseRepository.hasUserBoughtProduct(user, product);
    }

    public boolean hasUserBoughtProduct(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException());
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException());
        return purchaseRepository.hasUserBoughtProduct(user, product);
    }

    public List<PurchaseDTO> findByBuyerProduct(Long buyerId, Long productId) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new NoSuchElementException());
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException());
        return purchaseRepository.findByBuyerAndProduct(buyer, product)
                .stream()
                .map(purchaseMapper::toDTO)
                .collect(Collectors.toList());
    }
}
