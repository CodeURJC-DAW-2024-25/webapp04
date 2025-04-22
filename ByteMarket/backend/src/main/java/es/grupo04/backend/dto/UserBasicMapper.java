package es.grupo04.backend.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.grupo04.backend.model.User;

@Mapper(componentModel = "spring")
public interface UserBasicMapper {
    @Mapping(target = "image", expression = "java(mapProfileImage(user))")
    @Mapping(target = "hasImage", expression = "java(mapHasImage(user))")
    public UserBasicDTO toDTO(User user);
    
    default String mapProfileImage(User user) {
        return "/api/v1/users/"+ user.getId()+"/images";
    }
    
    default Boolean mapHasImage(User user) {
        return user.hasImage();
    }
}