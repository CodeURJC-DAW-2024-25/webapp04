package es.grupo04.backend.dto;

public record ReviewDTO(
    Long id,
    String description,
    UserBasicDTO reviewOwner,
    UserBasicDTO reviewedUser,
    Integer rating
) {
}
