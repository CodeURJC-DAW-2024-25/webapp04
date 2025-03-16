package es.grupo04.backend.dto;

public record MessageDTO(
    Long id,
    String sender,
    String message,
    String sentAt
) {
}
