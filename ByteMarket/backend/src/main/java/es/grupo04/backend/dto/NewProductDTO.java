package es.grupo04.backend.dto;

import org.springframework.web.multipart.MultipartFile;

public record NewProductDTO(
    String name,
    Float price,
    String category,
    String description,
    MultipartFile[] imageUpload
) {
}
