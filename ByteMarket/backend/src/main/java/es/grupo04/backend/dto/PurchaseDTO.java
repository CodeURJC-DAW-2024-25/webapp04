package es.grupo04.backend.dto;

import java.time.LocalDate;

public record PurchaseDTO(
    Long id, 
    Long productId, 
    Long buyerId, 
    Long sellerId, 
    LocalDate purchaseDate
    ) {
}

