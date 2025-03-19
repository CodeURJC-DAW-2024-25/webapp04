package es.grupo04.backend.dto;

import org.mapstruct.Mapper;

import es.grupo04.backend.model.User;

@Mapper(componentModel = "spring")
public class UserBasicMapper {

    public UserBasicDTO toDTO(User user) {
        return new UserBasicDTO(user.getId(), user.getName());
    }
    
}
