package es.grupo04.backend.dto;

import org.mapstruct.Mapper;

import es.grupo04.backend.model.Product;

@Mapper(componentModel = "spring")
public interface NewProductMapper {

    Product toDomain(NewProductDTO newProductDTO);

}