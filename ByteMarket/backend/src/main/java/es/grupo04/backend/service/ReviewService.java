package es.grupo04.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo04.backend.model.Review;
import es.grupo04.backend.repository.ReviewRepository;
import jakarta.transaction.Transactional;

@Service
public class ReviewService {
    
    @Autowired
    private ReviewRepository repository;

    public List<Review> getAllReviews() {
        return repository.findAll();
    }

    public Review saveReview(Review review) {
        return repository.save(review);
    }

    public Review getReviewById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID de la reseña no puede ser nulo");
        }
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review no encontrada con ID: " + id));
    }
    
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Review no encontrada con ID: " + id);
        }
        repository.deleteById(id);
        repository.flush();
    }
}
