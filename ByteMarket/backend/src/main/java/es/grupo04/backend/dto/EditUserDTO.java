package es.grupo04.backend.dto;

public record EditUserDTO (
    String name,
    String address,
    String newPass,
    String repeatPass,
    String iframe
){  
}