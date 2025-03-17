package es.grupo04.backend.service;

import java.util.List;

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
    private ReviewMapper mapper;

    public List<ReviewDTO> getAllReviews() {
        return mapper.toDTOs(repository.findAll());
    }

    public ReviewDTO getReviewById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID de la reseña no puede ser nulo");
        }
        Review review = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review no encontrada con ID: " + id));
        return mapper.toDTO(review);
    }

    public ReviewDTO saveReview(NewReviewDTO newReviewDTO, Long reviewOwnerId) {
    User reviewOwner = userRepository.findById(reviewOwnerId)
                      .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    User reviewedUser = userRepository.findById(newReviewDTO.reviewedUserId())
                      .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    Review review = new Review(reviewOwner, reviewedUser, 
                               newReviewDTO.description(), 
                               newReviewDTO.rating());

    repository.save(review);
    
    return mapper.toDTO(review);
    }


    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Review no encontrada con ID: " + id);
        }
        repository.deleteById(id);
    }
}
