package es.grupo04.backend.dto;

import java.util.List;



public record UserDTO (
    Long id,
    String name,
    Integer creationYear,
    List<String> roles,
    List<ProductBasicDTO> products,
    List<ReviewDTO> reviews,
    Integer salesNumber,
    Integer purchasesNumber,
    String iframe,
    String image,
    Boolean hasImage
    
){
}