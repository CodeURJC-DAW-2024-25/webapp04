package es.grupo04.backend.dto;

import java.util.List;

public record ChatDTO(
    Long id,
    ProductBasicDTO product,
    UserBasicDTO userBuyer,
    UserBasicDTO userSeller,
    List<MessageDTO> messages
) {
    
}
