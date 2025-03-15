package es.grupo04.backend.dto;

import org.mapstruct.*;

import es.grupo04.backend.model.Image;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.service.Constants;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "imageUrls", expression = "java(mapImages(product))")
    ProductDTO toDTO(Product product);

    List<ProductDTO> toDTOs(List<Product> products);

    default List<String> mapImages(Product product) {
        if (product.getImages() == null) {
            return null;
        }

        ArrayList<String> imageUrls = new ArrayList<>();
        for (Image image : product.getImages()) {
           imageUrls.add(Constants.WEBAPP_BASE_URL + "/product/image/" + image.getId());
        }
        return imageUrls;
    }
}
