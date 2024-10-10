package pl.luncher.v3.luncher_core.infrastructure.persistence;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.place.model.Place;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING, uses = {
    OpeningWindowMapper.class}, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
interface PlaceDbMapper {

  @Mapping(target = "name", source = "place.name")
  @Mapping(target = "owner", source = "owner")
  @Mapping(target = "placeType", source = "placeType")
  PlaceDb toDbEntity(Place place, UserDb owner, PlaceTypeDb placeType);

  @AfterMapping
  default void linkImages(@MappingTarget PlaceDb placeDb) {
    if (placeDb.getImages() == null) {
      return;
    }
    placeDb.getImages().forEach(image -> image.setPlace(placeDb));
  }

  @AfterMapping
  default void linkOpeningWindows(@MappingTarget PlaceDb placeDb) {
    if (placeDb.getOpeningWindows() == null) {
      return;
    }
    placeDb.getOpeningWindows().forEach(window -> window.setPlace(placeDb));
  }

  Place toDomain(PlaceDb placeDb);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  PlaceDb partialUpdate(
      Place place, @MappingTarget PlaceDb placeDb);
}
