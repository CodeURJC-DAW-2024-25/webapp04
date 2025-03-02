package es.grupo04.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.grupo04.backend.model.Review;
import es.grupo04.backend.repository.ReviewRepository;

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
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review no encontrada con ID: " + id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
