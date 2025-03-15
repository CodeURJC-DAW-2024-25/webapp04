package es.grupo04.backend.dto;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.grupo04.backend.model.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "reviewOwner.id", target = "reviewOwnerId")
    @Mapping(source = "reviewedUser.id", target = "reviewedUserId")
    ReviewDTO toDTO(Review review);

    List<ReviewDTO> toDTOs(Collection<Review> reviews);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviewOwner", ignore = true) 
    @Mapping(target = "reviewedUser", ignore = true) 
    Review toEntity(ReviewDTO reviewDTO);
}
