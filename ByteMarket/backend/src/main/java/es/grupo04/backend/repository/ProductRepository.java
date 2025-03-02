

package es.grupo04.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<Product> findByCategory(String category, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.sold = false")
    public Page<Product> findAllByAvailableTrue(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.sold = false")
    public Page<Product> findByCategoryAndAvailableTrue(@Param("category") String category, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(FUNCTION('UNACCENT', p.name)) LIKE LOWER(FUNCTION('UNACCENT', CONCAT('%', :searchTerm, '%')))")
    List<Product> searchByName(@Param("searchTerm") String searchTerm);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

}


