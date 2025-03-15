package es.grupo04.backend.dto;

public record ReportDTO(
    Long id,
    String reason,
    String description,
    Long productId,
    Long userId
) {
}
