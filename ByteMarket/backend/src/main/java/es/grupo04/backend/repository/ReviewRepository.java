package es.grupo04.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.grupo04.backend.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
