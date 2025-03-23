package es.grupo04.backend.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.grupo04.backend.model.Image;
import es.grupo04.backend.service.Constants;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Mapping(target = "imageURL", expression = "java(mapProfileImage(image))")
    ImageDTO toDTO(Image image);

    default String mapProfileImage(Image image) {
        return Constants.WEBAPP_BASE_URL + "/product/image/" + image.getId();
    }
    
}
