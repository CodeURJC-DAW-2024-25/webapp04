package es.grupo04.backend.dto;

import java.time.LocalDate;

public record ProductBasicDTO(
    Long id,
    String name,
    Float price,
    String category,
    boolean sold,
    LocalDate publishDate
){
}
