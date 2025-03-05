

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
    Page<Product> findProductByOwner(User owner, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p IN :favorites")
    Page<Product> findFavoriteProducts(@Param("favorites") List<Product> favorites, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.users u WHERE u = :user")
    List<Product> findFavoritesByUser(@Param("user") User user);

    @Query("SELECT u FROM UserTable u JOIN u.favoriteProducts p WHERE p = :product")
    List<User> findUsersByFavoriteProduct(@Param("product") Product product);

    Page<Product> findByCategory(String category, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.sold = false ORDER BY p.publishDate DESC")
    public Page<Product> findAllByAvailableTrue(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.sold = false")
    public Page<Product> findByCategoryAndAvailableTrue(@Param("category") String category, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(FUNCTION('UNACCENT', p.name)) LIKE LOWER(FUNCTION('UNACCENT', CONCAT('%', :searchTerm, '%')))")
    List<Product> searchByName(@Param("searchTerm") String searchTerm);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query(value = """
        WITH SellerRatings AS (
            SELECT r.reviewed_user_id AS seller_id, COALESCE(AVG(r.rating), 0) AS avg_rating
            FROM Review r
            GROUP BY r.reviewed_user_id
        ),
        RankedProducts AS (
            SELECT p.*, sr.avg_rating,
                   ROW_NUMBER() OVER (PARTITION BY p.owner_id ORDER BY p.publish_date DESC) AS product_rank
            FROM Product p
            JOIN SellerRatings sr ON p.owner_id = sr.seller_id
            WHERE p.sold = false
        )
        SELECT * FROM RankedProducts
        WHERE product_rank <= 3
        ORDER BY avg_rating DESC, publish_date DESC
    """, nativeQuery = true)
    List<Product> findTopRatedSellersProducts();







}


