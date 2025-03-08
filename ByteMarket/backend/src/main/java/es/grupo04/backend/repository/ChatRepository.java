package es.grupo04.backend.repository;

import es.grupo04.backend.model.Chat;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByUserBuyer(User userBuyer);

    List<Chat> findByUserSeller(User userSeller);

    @Query("SELECT c FROM Chat c WHERE ((c.userBuyer = :buyer AND c.userSeller = :seller) OR (c.userBuyer = :seller AND c.userSeller = :buyer)) AND c.product.id = :productId")
    Chat findByUsersAndProduct(@Param("buyer") User buyer, @Param("seller") User seller, @Param("productId") Long productId);
    

    Chat findByUserBuyerAndUserSellerAndProduct(User userBuyer, User userSeller, Product product);

}
