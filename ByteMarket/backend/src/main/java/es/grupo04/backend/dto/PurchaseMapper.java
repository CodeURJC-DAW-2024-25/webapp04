package es.grupo04.backend.dto;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.grupo04.backend.model.Purchase;

@Mapper(componentModel = "spring", uses = {ProductBasicMapper.class, UserBasicMapper.class})
public interface PurchaseMapper {

    PurchaseDTO toDTO(Purchase purchase);

    List<PurchaseDTO> toDTOs(Collection<Purchase> purchases);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true) 
    @Mapping(target = "buyer", ignore = true) 
    @Mapping(target = "seller", ignore = true) 
    Purchase toEntity(PurchaseDTO purchaseDTO);
}
