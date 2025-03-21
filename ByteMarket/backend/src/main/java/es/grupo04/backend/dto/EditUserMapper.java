package es.grupo04.backend.dto;

import org.mapstruct.Mapper;

import es.grupo04.backend.model.User;

@Mapper(componentModel = "spring", uses = {ProductBasicMapper.class, ReviewMapper.class})
public interface EditUserMapper {

    EditUserDTO toDTO(User user);
    
}