package es.grupo04.backend.dto;

public record ReviewDTO(
    Long id,
    String description,
    Long reviewOwnerId,
    Long reviewedUserId,
    Integer rating
) {
}
