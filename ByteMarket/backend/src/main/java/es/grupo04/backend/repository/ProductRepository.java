

package es.grupo04.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.User;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByOwner(User owner);

    @Query("SELECT p FROM Product p JOIN p.users u WHERE u = :user")
    List<Product> findFavoritesByUser(@Param("user") User user);

    @Query("SELECT u FROM UserTable u JOIN u.favoriteProducts p WHERE p = :product")
    List<User> findUsersByFavoriteProduct(@Param("product") Product product);

    List<Product> findByCategory(String category);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> searchByName(@Param("searchTerm") String searchTerm);

}


