package es.grupo04.backend.dto;

public record EditUserDTO (
    Long id,
    String name,
    String address,
    String password,
    String repeatPassword,
    String iframe,
    byte[] imageFile,    //User Image as byte --> Turned to blob in the service
    boolean image       
){  
}
 