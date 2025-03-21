package es.grupo04.backend.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.grupo04.backend.model.User;

@Mapper(componentModel = "spring", uses = {ProductBasicMapper.class, ReviewMapper.class})
public interface EditUserMapper {
    @Mapping(target = "hasImage", expression = "java(mapHasImage(user))")
    EditUserDTO toDTO(User user);
    
    default Boolean mapHasImage(User user) {
        return user.hasImage();
    }
    
}