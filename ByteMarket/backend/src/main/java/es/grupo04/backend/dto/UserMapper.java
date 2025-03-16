package es.grupo04.backend.dto;

import java.util.List;

import org.mapstruct.Mapper;

import es.grupo04.backend.model.User;

@Mapper(componentModel = "spring", uses = {ProductBasicMapper.class, ReviewMapper.class})
public interface UserMapper {

    UserDTO toDTO(User user);

    List<UserDTO> toDTOs(List<User> users);
    
}
