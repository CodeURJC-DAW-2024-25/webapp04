package es.grupo04.backend.dto;

import org.mapstruct.*;

import es.grupo04.backend.model.Image;
import es.grupo04.backend.model.Product;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {UserBasicMapper.class})
public interface ProductMapper {

    @Mapping(target = "imageUrls", expression = "java(mapImages(product))")
    @Mapping(target = "thumbnail", expression = "java(mapThumbnail(product))")
    ProductDTO toDTO(Product product);

    List<ProductDTO> toDTOs(List<Product> products);

    default List<String> mapImages(Product product) {
        if (product.getImages() == null) {
            return null;
        }

        ArrayList<String> imageUrls = new ArrayList<>();
        for (Image image : product.getImages()) {
           imageUrls.add("/api/v1/products/" + product.getId() + "/images/" + image.getId());
        }
        return imageUrls;
    }

    default String mapThumbnail(Product product) {
        return "/api/v1/products/" + product.getId() + "/images/" + product.getImages().get(0).getId();
    }
    
}
