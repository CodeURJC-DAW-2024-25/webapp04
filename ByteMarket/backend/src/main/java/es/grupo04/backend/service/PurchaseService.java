package es.grupo04.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo04.backend.dto.PurchaseDTO;
import es.grupo04.backend.dto.PurchaseMapper;
import es.grupo04.backend.model.Chat;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.Purchase;
import es.grupo04.backend.model.User;
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
    private PurchaseMapper purchaseMapper; // Inyectamos PurchaseMapper

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

    public PurchaseDTO createPurchase(Chat chat) {
        User buyer = chat.getUserBuyer();
        User seller = chat.getUserSeller();
        Product product = chat.getProduct();
        
        if (product.isSold()) {
            return null;
        }

        // Actualizar el estado del producto
        product.setSold(true);
        productService.sold(product, buyer);
        productRepository.saveAndFlush(product);

        // Creación de la compra
        Purchase purchase = new Purchase(product, buyer, seller);        
        purchaseRepository.saveAndFlush(purchase);

        // Actualización de usuarios
        buyer.addPurchase(purchase);
        seller.addSale(purchase);
        userRepository.save(buyer);
        userRepository.save(seller);

        return purchaseMapper.toDTO(purchase);
    }

    public List<PurchaseDTO> findByBuyer(User buyer) {
        return purchaseRepository.findByBuyerOrderByPurchaseDateDesc(buyer)
                .stream()
                .map(purchaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean hasBought(User user, User owner) {
        return !purchaseRepository.findByBuyerAndSeller(user, owner).isEmpty();
    }

    public boolean hasUserBoughtProduct(User user, Product product) {
        return purchaseRepository.hasUserBoughtProduct(user, product);
    }
}
