package pl.luncher.v3.luncher_core.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.Comparator;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import pl.luncher.v3.luncher_core.common.interfaces.LocalDateTimeProvider;
import pl.luncher.v3.luncher_core.common.model.timing.WeekDayTimeRange;
import pl.luncher.v3.luncher_core.place.model.Place;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING, uses = {
    WeekDayTimeRangeMapper.class, PlaceTypeDbMapper.class, AssetDbMapper.class,
    MenuOfferDbMapper.class}, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
abstract class PlaceDbMapper {

  @Autowired
  private LocalDateTimeProvider localDateTimeProvider;

  @Mapping(target = "name", source = "place.name")
  @Mapping(target = "owner", source = "owner")
  @Mapping(target = "placeType", source = "placeType")
  @Mapping(target = "enabled", source = "place.enabled")
  abstract PlaceDb toDb(Place place, UserDb owner, PlaceTypeDb placeType);

  @AfterMapping
  void linkChildEntities(@MappingTarget PlaceDb placeDb) {
    if (placeDb.getImages() != null) {
      placeDb.getImages().forEach(image -> image.setPlace(placeDb));
    }
  }

  @AfterMapping
  void assignAssetsIndexes(@MappingTarget PlaceDb placeDb) {
    if (placeDb.getImages() == null) {
      return;
    }
    for (int i = 0; i < placeDb.getImages().size(); i++) {
      placeDb.getImages().get(i).setPlaceImageIdx(i);
    }
  }

  @AfterMapping
  void sortOpeningWindows(@MappingTarget Place place) {
    if (place.getOpeningWindows() != null) {
      place.getOpeningWindows()
          .sort(Comparator.comparing(WeekDayTimeRange::getStartTime));
    }
  }

  @AfterMapping
  void sortMenuOffers(@MappingTarget Place place) {
    if (place.getMenuOffers() != null) {
      var time = localDateTimeProvider.now(place.getTimeZone());
      place.getMenuOffers()
          .sort(Comparator.comparing((menuOffer -> {
            LocalDateTime soonestServingTime = menuOffer.getSoonestServingTime(time);
            return soonestServingTime == null ? LocalDateTime.of(3000, 1, 1, 0, 0)
                : soonestServingTime;
          })));
    }
  }

  abstract Place toDomain(PlaceDb placeDb);
}
