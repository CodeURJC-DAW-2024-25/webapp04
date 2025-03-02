package es.grupo04.backend.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Purchase save(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public Optional<Purchase> findById(Long id) {
        return purchaseRepository.findById(id);
    }

    public List<Purchase> findAll() {
        return purchaseRepository.findAll();
    }

    public Purchase createPurchase(Chat chat){
        User buyer = chat.getUserBuyer();
        User seller = chat.getUserSeller();
        Product product = chat.getProduct();
        if(product.isSold()){
            return null;
        }
        //Update the product state
        product.setSold(true);
        productService.sold(product, buyer);
        productRepository.saveAndFlush(product);
        //Purchase Creation
        Purchase purchase = new Purchase(product,buyer,seller);        
        purchaseRepository.saveAndFlush(purchase);
        //Users update
        buyer.addPurchase(purchase);
        seller.addSale(purchase);
        userRepository.save(buyer);
        userRepository.save(seller);
        return purchase;
    }

    public List<Purchase> findByBuyer(User buyer){
        return purchaseRepository.findByBuyerOrderByPurchaseDateDesc(buyer);
    }

    public boolean hasBought(User user, User owner) {
        return !purchaseRepository.findByBuyerAndSeller(user, owner).isEmpty();
    }
}
