package es.grupo04.backend.dto;

public record MessageDTO(
    Long id,
    UserBasicDTO sender,
    String message,
    String sentAt
) {
}
