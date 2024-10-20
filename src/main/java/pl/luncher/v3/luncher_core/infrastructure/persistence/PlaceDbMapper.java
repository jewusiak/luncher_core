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
    WeekDayTimeRangeMapper.class, PlaceTypeDbMapper.class, AssetDbMapper.class,
    MenuOfferDbMapper.class}, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
interface PlaceDbMapper {

  @Mapping(target = "name", source = "place.name")
  @Mapping(target = "owner", source = "owner")
  @Mapping(target = "placeType", source = "placeType")
  @Mapping(target = "enabled", source = "place.enabled")
  PlaceDb toDb(Place place, UserDb owner, PlaceTypeDb placeType);

  @AfterMapping
  default void linkChildEntities(@MappingTarget PlaceDb placeDb) {
    if (placeDb.getImages() != null) {
      placeDb.getImages().forEach(image -> image.setPlace(placeDb));
    }
    if (placeDb.getOpeningWindows() != null) {
      placeDb.getOpeningWindows().forEach(window -> window.setPlace(placeDb));
    }
    if (placeDb.getMenuOffers() != null) {
      placeDb.getMenuOffers().forEach(menuOfferDb -> menuOfferDb.setPlace(placeDb));
    }
  }

  Place toDomain(PlaceDb placeDb);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  PlaceDb partialUpdate(
      Place place, @MappingTarget PlaceDb placeDb);
}
