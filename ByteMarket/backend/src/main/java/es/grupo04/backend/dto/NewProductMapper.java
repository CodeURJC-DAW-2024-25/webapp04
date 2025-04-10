package es.grupo04.backend.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.grupo04.backend.model.Product;

@Mapper(componentModel = "spring")
public interface NewProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "favoriteOfUsers", ignore = true)
    @Mapping(target = "imageFile", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "publishDate", ignore = true)
    @Mapping(target = "reports", ignore = true)
    @Mapping(target = "sold", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "hasImage", ignore = true)
    Product toDomain(NewProductDTO newProductDTO);

}