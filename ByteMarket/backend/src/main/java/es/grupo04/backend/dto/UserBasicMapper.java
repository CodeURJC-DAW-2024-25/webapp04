package es.grupo04.backend.dto;

import org.mapstruct.Mapper;

import es.grupo04.backend.model.User;

@Mapper(componentModel = "spring")
public interface UserBasicMapper {

    public UserBasicDTO toDTO(User user);
    
}