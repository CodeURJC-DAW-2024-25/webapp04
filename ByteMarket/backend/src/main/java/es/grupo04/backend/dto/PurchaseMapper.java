package es.grupo04.backend.dto;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.grupo04.backend.model.Purchase;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "buyer.id", target = "buyerId")
    @Mapping(source = "seller.id", target = "sellerId")
    PurchaseDTO toDTO(Purchase purchase);

    List<PurchaseDTO> toDTOs(Collection<Purchase> purchases);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true) 
    @Mapping(target = "buyer", ignore = true) 
    @Mapping(target = "seller", ignore = true) 
    Purchase toEntity(PurchaseDTO purchaseDTO);
}
