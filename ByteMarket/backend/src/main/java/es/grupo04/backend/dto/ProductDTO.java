package es.grupo04.backend.dto;

import java.time.LocalDate;
import java.util.List;

public record ProductDTO(
    Long id,
    String name,
    Float price,
    String category,
    String description,
    UserBasicDTO owner, //Avoid returning the complete User object
    List<String> imageUrls,
    boolean sold,
    LocalDate publishDate
){
}
