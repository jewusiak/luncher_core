package pl.luncher.v3.luncher_core.common.persistence.infrastructure;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.common.domain.place.model.dtos.PlaceDto;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface PlaceDbMapper {

  @Mapping(source = "ownerEmail", target = "owner.email")
  PlaceDb toEntity(PlaceDto placeDto);

  @AfterMapping
  default void linkImages(@MappingTarget PlaceDb placeDb) {
    placeDb.getImages().forEach(image -> image.setRefToPlaceImages(placeDb));
  }

  @Mapping(source = "owner.email", target = "ownerEmail")
  PlaceDto toDto(PlaceDb placeDb);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(source = "ownerEmail", target = "owner.email")
  PlaceDb partialUpdate(PlaceDto placeDto, @MappingTarget PlaceDb placeDb);
}