package es.grupo04.backend.dto;

import java.util.List;

import org.mapstruct.Mapper;
import es.grupo04.backend.model.Product;

@Mapper(componentModel = "spring")
public interface ProductBasicMapper {
    
    ProductBasicDTO toDTO(Product product);

    List<ProductBasicDTO> toDTOs(List<Product> products);

}
