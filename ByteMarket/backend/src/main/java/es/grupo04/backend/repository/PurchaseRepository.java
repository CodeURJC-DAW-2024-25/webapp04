package es.grupo04.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.grupo04.backend.model.Purchase;
import es.grupo04.backend.model.User;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
        
    List<Purchase> findByBuyerOrderByPurchaseDateDesc(User buyer);

    List<Purchase> findBySellerOrderByPurchaseDateDesc(User seller);
}
