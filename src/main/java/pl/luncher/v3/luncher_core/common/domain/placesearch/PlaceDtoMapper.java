package pl.luncher.v3.luncher_core.common.domain.placesearch;

import org.locationtech.jts.geom.Point;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.common.domain.placesearch.dtos.PlaceSearchDto;
import pl.luncher.v3.luncher_core.common.model.dto.Location;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface PlaceDtoMapper {

  @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  PlaceSearchDto toDto(PlaceDb placeDb);

  default Location mapToLocation(Point point) {
    return new Location(point.getY(), point.getX());
  }
}