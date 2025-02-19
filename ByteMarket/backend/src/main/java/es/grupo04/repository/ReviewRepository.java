package es.grupo04.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.grupo04.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
