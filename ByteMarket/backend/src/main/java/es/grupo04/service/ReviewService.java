package es.grupo04.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import es.grupo04.model.Review;
import es.grupo04.repository.ReviewRepository;

public class ReviewService {
    
    @Autowired
	private ReviewRepository repository;

    public List<Review> getAllReviews() {
        return repository.findAll();
    }

    public Review saveReview(Review Review) {
        return repository.save(Review);
    }

    public Review getReviewById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review no encontrada con ID: " + id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
