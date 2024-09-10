package pl.luncher.v3.luncher_core.common.domain.place;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.common.domain.users.User;
import pl.luncher.v3.luncher_core.common.model.dto.Location;
import pl.luncher.v3.luncher_core.common.model.dto.OpeningWindowDto;
import pl.luncher.v3.luncher_core.common.model.requests.PlaceCreateRequest;
import pl.luncher.v3.luncher_core.common.model.requests.PlaceUpdateRequest;
import pl.luncher.v3.luncher_core.common.model.responses.BasicPlaceResponse;
import pl.luncher.v3.luncher_core.common.model.responses.FullPlaceResponse;
import pl.luncher.v3.luncher_core.common.model.valueobjects.WeekDayTime;
import pl.luncher.v3.luncher_core.common.persistence.models.OpeningWindowDb;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceTypeDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceTypeRepository;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, unmappedSourcePolicy = ReportingPolicy.WARN, componentModel = ComponentModel.SPRING)
abstract class PlaceDbMapper {

  @Mapping(target = "owner", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "images", ignore = true)
  @Mapping(target = "name", source = "request.name")
  @Mapping(target = "placeType", source = "placeTypeIdentifier", qualifiedByName = "resolvePlaceTypeDb")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  abstract void updateWith(@MappingTarget PlaceDb placeDb, PlaceUpdateRequest request,
      @Context PlaceTypeRepository placeTypeRepository);

  @BeanMapping(ignoreByDefault = true)
  abstract BasicPlaceResponse mapToBasic(PlaceDb placeDb);

  abstract FullPlaceResponse mapToFull(PlaceDb placeDb);

  @Mapping(target = "uuid", ignore = true)
  abstract OpeningWindowDb mapToDb(OpeningWindowDto openingWindow, PlaceDb place);

  @BeanMapping(ignoreUnmappedSourceProperties = "place")
  abstract OpeningWindowDto mapToDto(OpeningWindowDb openingWindowDb);


  @Mapping(target = "images", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "name", source = "request.name")
  @Mapping(target = "placeType", source = "request.placeTypeIdentifier", qualifiedByName = "resolvePlaceTypeDb")
  @Mapping(target = "owner", source = "owner.dbEntity")
  abstract PlaceDb fromCreation(PlaceCreateRequest request, User owner,
      @Context PlaceTypeRepository placeTypeRepository);

  @AfterMapping
  void ensureOpeningWindowsHavePlaceReference(@MappingTarget PlaceDb placeDb) {
    if (placeDb.getStandardOpeningTimes() != null) {
      placeDb.getStandardOpeningTimes().forEach(e -> e.setPlace(placeDb));
    }
  }


  @Named("resolvePlaceTypeDb")
  PlaceTypeDb resolvePlaceTypeDb(String placeTypeIdentifier, @Context PlaceTypeRepository placeTypeRepository) {
    return placeTypeRepository.findById(placeTypeIdentifier).orElseThrow();
  }

  Point mapToPoint(Location location) {
    return new GeometryFactory().createPoint(
        new Coordinate(location.getLongitude(), location.getLatitude()));
  }

  Location mapToLocation(Point point) {
    return new Location(point.getY(), point.getX());
  }

  int mapToInt(WeekDayTime time) {
    return time.toIntTime();
  }

  WeekDayTime toWeekDayTime(Integer time) {
    return WeekDayTime.of(time);
  }
}
