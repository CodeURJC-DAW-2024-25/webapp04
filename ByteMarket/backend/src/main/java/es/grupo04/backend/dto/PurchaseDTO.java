package es.grupo04.backend.dto;

import java.time.LocalDate;

public record PurchaseDTO(
    Long id, 
    ProductBasicDTO product, 
    UserBasicDTO buyer, 
    UserBasicDTO seller,
    LocalDate purchaseDate
    ) {
}

