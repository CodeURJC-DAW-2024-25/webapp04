package es.grupo04.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.grupo04.backend.dto.NewReviewDTO;
import es.grupo04.backend.dto.ReviewDTO;
import es.grupo04.backend.dto.ReviewMapper;
import es.grupo04.backend.model.Review;
import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.ReviewRepository;
import es.grupo04.backend.repository.UserRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ReviewMapper mapper;

    public List<ReviewDTO> getAllReviews() {
        return mapper.toDTOs(repository.findAll());
    }

    public List<ReviewDTO> getAllReviewsByUserId(Long userId) {
        return mapper.toDTOs(repository.findByReviewedUserId(userId));
    }

    public ReviewDTO getReviewById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Review ID cannot be null");
        }
        Review review = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException());
        return mapper.toDTO(review);
    }

    public Optional<ReviewDTO> saveReview(NewReviewDTO newReviewDTO, Long reviewOwnerId) {
    User reviewOwner = userRepository.findById(reviewOwnerId)
                      .orElseThrow(() -> new NoSuchElementException());

    User reviewedUser = userRepository.findById(newReviewDTO.reviewedUserId())
                      .orElseThrow(() -> new NoSuchElementException());

    if(!purchaseService.hasBought(reviewOwnerId, newReviewDTO.reviewedUserId())) {
        return Optional.empty();
    }

    Review review = new Review(reviewOwner, reviewedUser, 
                               newReviewDTO.description(), 
                               newReviewDTO.rating());

    repository.save(review);
    
    return Optional.of(mapper.toDTO(review));
    }


    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException();
        }
        repository.deleteById(id);
    }

    public List<Map<String, Object>> getReviewsInfo(List<ReviewDTO> reviews) {
        List<Map<String, Object>> reviewStars = new ArrayList<>();
        for (ReviewDTO review : reviews) {
            int rating = review.rating();
            List<Boolean> stars = new ArrayList<>();
            List<Boolean> emptyStars = new ArrayList<>();
            
            // Stars full
            for (int i = 0; i < rating; i++) {
                stars.add(true);
            }

            // Stars empty
            for (int i = rating; i < 5; i++) {
                emptyStars.add(false);
            }

            Map<String, Object> reviewStarData = new HashMap<>();
            reviewStarData.put("rating", rating);
            reviewStarData.put("stars", stars); // Full stars list
            reviewStarData.put("emptyStars", emptyStars); // Empty stars list
            reviewStarData.put("owner", review.reviewOwner().name());
            reviewStarData.put("description", review.description());
            reviewStars.add(reviewStarData);
        }

        return reviewStars;
    }
}
