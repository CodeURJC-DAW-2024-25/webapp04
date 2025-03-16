package es.grupo04.backend.dto;

public record NewUserDTO (
    String mail,
    String name,
    String password,
    String repeatPassword
){  
}
