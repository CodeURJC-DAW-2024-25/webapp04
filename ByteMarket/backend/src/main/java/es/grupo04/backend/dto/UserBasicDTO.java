package es.grupo04.backend.dto;

/* UserBasicDTO is a DTO that represents a user with only their name and id
*  so you can request the user profile by this id
*/ 
public record UserBasicDTO(
    Long id,
    String name
) {
}
