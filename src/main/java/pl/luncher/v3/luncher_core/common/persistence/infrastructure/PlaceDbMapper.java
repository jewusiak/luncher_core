package pl.luncher.v3.luncher_core.common.persistence.infrastructure;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.common.domain.place.domain.dtos.PlaceDto;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb;
import pl.luncher.v3.luncher_core.common.persistence.models.UserDb;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = ComponentModel.SPRING)
public interface PlaceDbMapper {

  @Mapping(source = "owner", target = "owner")
  PlaceDb toEntity(PlaceDto placeDto, UserDb owner);

  @AfterMapping
  default void linkImages(@MappingTarget PlaceDb placeDb) {
    if (placeDb.getImages() == null) {
      return;
    }
    placeDb.getImages().forEach(image -> image.setRefToPlaceImages(placeDb));
  }

  @AfterMapping
  default void linkOpeningWindows(@MappingTarget PlaceDb placeDb) {
    if (placeDb.getStandardOpeningTimes() == null) {
      return;
    }
    placeDb.getStandardOpeningTimes().forEach(ow -> ow.setPlace(placeDb));
  }

  @Mapping(source = "owner.email", target = "ownerEmail")
  PlaceDto toDto(PlaceDb placeDb);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(source = "ownerEmail", target = "owner.email")
  PlaceDb partialUpdate(PlaceDto placeDto, @MappingTarget PlaceDb placeDb);
}