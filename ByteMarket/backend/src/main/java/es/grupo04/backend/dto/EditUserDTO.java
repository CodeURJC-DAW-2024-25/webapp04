package es.grupo04.backend.dto;

import org.springframework.web.multipart.MultipartFile;

public record EditUserDTO (
    Long id,
    String mail,
    String name,
    String address,
    String newPass,
    String repeatPass,
    String iframe,
    MultipartFile profilePicInput,
    Boolean hasImage
){  
}
 