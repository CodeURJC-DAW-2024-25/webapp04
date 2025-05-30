package es.grupo04.backend.dto;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.grupo04.backend.model.User;

@Mapper(componentModel = "spring", uses = {ProductBasicMapper.class, ReviewMapper.class, UserBasicMapper.class})
public interface UserMapper {
    @Mapping(target = "image", expression = "java(mapProfileImage(user))")
    @Mapping(target = "hasImage", expression = "java(mapHasImage(user))")
    // @Mapping(target = "salesNumber", expression = "java(user.getSales().size())")
    // @Mapping(target = "purchasesNumber", expression = "java(user.getPurchases().size())")
    UserDTO toDTO(User user);

    List<UserDTO> toDTOs(List<User> users);

    default String mapProfileImage(User user) {
        return "/api/v1/users/"+ user.getId()+"/images";
    }
    default Boolean mapHasImage(User user) {
        return user.hasImage();
    }

}
