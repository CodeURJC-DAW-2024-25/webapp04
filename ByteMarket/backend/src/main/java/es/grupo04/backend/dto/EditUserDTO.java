package es.grupo04.backend.dto;

import org.springframework.web.multipart.MultipartFile;

public record EditUserDTO (
    Long id,
    String name,
    String address,
    String password,
    String repeatPassword,
    String iframe,
    String profileImage,
    MultipartFile image
){  
}
 