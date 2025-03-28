package es.grupo04.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.Purchase;
import es.grupo04.backend.model.User;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
        
    List<Purchase> findByBuyerOrderByPurchaseDateDesc(User buyer);

    List<Purchase> findBySellerOrderByPurchaseDateDesc(User seller);

    List<Purchase> findByBuyerAndSeller(User buyer, User seller);

    List<Purchase> findByBuyerOrSellerOrderByPurchaseDateDesc(User buyer, User seller);

    @Query("SELECT COUNT(p) > 0 FROM Purchase p WHERE p.buyer = :user AND p.product = :product")
    boolean hasUserBoughtProduct(@Param("user") User user, @Param("product") Product product);
}
