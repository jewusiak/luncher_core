package pl.luncher.v3.luncher_core.common.domain.placesearch;

import jakarta.persistence.EntityManager;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.common.model.requests.PlaceSearchRequest;
import pl.luncher.v3.luncher_core.common.model.valueobjects.WeekDayTime;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceRepository;

@Component
@RequiredArgsConstructor
public class PlaceSearchFactory {

  private final PlaceRepository placeRepository;
  private final PlaceDtoMapper placeDtoMapper;
  private final EntityManager entityManager;


  public PlaceSearch of(PlaceSearchRequest request) {
//
//    if (Stream.of(request.getOpenAt(), request.getPlaceTypeIdentifier(), request.getTextQuery(),
//        request.getLocation(), request.getDayOfWeek()).noneMatch(
//        Objects::nonNull)) {
//      throw new IllegalArgumentException("No search filters!");
//    }
//    
    if (Stream.of(request.getDayOfWeek(), request.getOpenAt()).filter(Objects::isNull).count()
        == 1) {
      throw new IllegalArgumentException(
          "You need to define both dayOfWeek and openAt or leave both as null");
    }

    LocationWithRadius location = null;
    if (request.getLocation() != null) {
      location = LocationWithRadius.of(request.getLocation().getLatitude(),
          request.getLocation().getLongitude(), request.getLocation().getRadius());
    }

//    return new PlaceSearchWithPostgres(request.getTextQuery(), request.getPlaceTypeIdentifier(),
//        dayOfWeek, request.getOpenAt(), location, placeRepository, placeDtoMapper);

    WeekDayTime openAt = null;

    if (request.getOpenAt() != null && request.getDayOfWeek() != null) {
      openAt = WeekDayTime.of(request.getDayOfWeek(), request.getOpenAt());
    }

    return new PlaceSearchWithHibernateSearch(request.getTextQuery(),
        request.getPlaceTypeIdentifier(), openAt, location, entityManager, placeDtoMapper);
  }
}
