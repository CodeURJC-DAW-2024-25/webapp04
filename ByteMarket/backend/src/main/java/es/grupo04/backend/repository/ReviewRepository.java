package es.grupo04.backend.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.grupo04.backend.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Collection<Review> findByReviewedUserId(Long userId);

}
