package es.grupo04.backend.dto;

public record NewReviewDTO(
    Integer rating, 
    String description,
    Long reviewedUserId 
) { 
}
