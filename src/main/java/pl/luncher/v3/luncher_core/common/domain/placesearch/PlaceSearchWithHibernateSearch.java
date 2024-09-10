package pl.luncher.v3.luncher_core.common.domain.placesearch;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.mapper.orm.Search;
import pl.luncher.v3.luncher_core.common.domain.placesearch.dtos.PlaceSearchDto;
import pl.luncher.v3.luncher_core.common.model.valueobjects.WeekDayTime;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb;

/**
 * Proof of concept implementation - works, but does not support real full-text search
 */
@Builder
@Data
class PlaceSearchWithHibernateSearch implements PlaceSearch {

  private final String textQuery;
  private final String placeTypeIdentifier;
  private final WeekDayTime openAt;

  private final LocationWithRadius location;

  private final EntityManager entityManager;
  private final PlaceDtoMapper placeDtoMapper;

  @Override
  public List<PlaceSearchDto> fetch(int page, int size) {

    var query = Search.session(entityManager).search(PlaceDb.class).where((f, root) -> {
      root.add(f.matchAll());

      if (textQuery != null) {
        root.add(f.match().fields("name", "longName", "description").matching(textQuery)
            .fuzzy(textQuery.length() > 4 ? 2 : 1));
      }

      if (placeTypeIdentifier != null) {
        root.add(f.match().field("placeType.identifier").matching(placeTypeIdentifier));
      }

      if (openAt != null) {
        // porządnie przetestować 

        SearchPredicate baseTimePredicate = f.and(
            f.range().field("standardOpeningTimes.startTime").atLeast(openAt.toIntTime()),
            f.range().field("standardOpeningTimes.endTime").lessThan(openAt.toIntTime())
        ).toPredicate();

        SearchPredicate incrementedTimePredicate = f.and(
            f.range().field("openingWindows.startTime").atLeast(openAt.toIncrementedIntTime()),
            f.range().field("openingWindows.endTime").lessThan(openAt.toIncrementedIntTime())
        ).toPredicate();

        root.add(f.or(baseTimePredicate, incrementedTimePredicate));
      }

      if (location != null) {
        root.add(f.spatial().within().field("location").circle(location.getLocation(),
            location.getRadius()));
      }

    });

    return query.fetch(page * size, size).hits().stream().map(placeDtoMapper::toDto).toList();
  }
}
