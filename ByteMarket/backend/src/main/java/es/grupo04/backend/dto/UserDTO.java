package es.grupo04.backend.dto;

import java.util.List;



public record UserDTO (
    Long id,
    String name,
    Integer creationYear,
    List<String> roles,
    List<ProductBasicDTO> products,
    List<ReviewDTO> reviews,
    List<ProductBasicDTO> sales,
    List<ProductBasicDTO> purchases,
    String iframe,
    String image,
    Boolean hasImage
    
){
}