package pl.luncher.v3.luncher_core.common.domain.placesearch;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import pl.luncher.v3.luncher_core.common.domain.placesearch.dtos.PlaceSearchDto;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceRepository;

/**
 * Proof of concept implementation - works, but does not support real full-text search
 */
@Builder
@Data
class PlaceSearchWithPostgres implements PlaceSearch {

  private final String textQuery;
  private final String placeTypeIdentifier;
  private final DayOfWeek dayOfWeek;
  private final LocalTime openAt;

  private final LocationWithRadius location;

  private final PlaceRepository placeRepository;
  private final PlaceDtoMapper placeDtoMapper;

  @Override
  public List<PlaceSearchDto> fetch(int page, int size) {
    Point point = location == null ? null : location.getLocation().toPoint();
    Double radius = location == null ? null : location.getRadius();

    List<PlaceDb> placeDbs = placeRepository.searchQuery(textQuery, placeTypeIdentifier, dayOfWeek,
        openAt, point,
        radius, size, page * size);
    return placeDbs.stream().map(placeDtoMapper::toDto).toList();
  }
}
