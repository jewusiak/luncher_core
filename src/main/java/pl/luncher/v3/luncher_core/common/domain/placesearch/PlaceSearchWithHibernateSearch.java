package pl.luncher.v3.luncher_core.common.domain.placesearch;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.mapper.orm.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.luncher.v3.luncher_core.common.domain.placesearch.dtos.PlaceSearchDto;
import pl.luncher.v3.luncher_core.common.model.valueobjects.WeekDayTime;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb;

/**
 * Proof of concept implementation - works, but does not support real full-text search
 */
@Builder
@Data
class PlaceSearchWithHibernateSearch implements PlaceSearch {

  private static final Logger log = LoggerFactory.getLogger(PlaceSearchWithHibernateSearch.class);
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

        root.add(f.or(f.match().fields("description").matching(textQuery)
            /*.fuzzy(1)*/, f.match().fields("name", "longName").matching(textQuery)));
      }

      if (placeTypeIdentifier != null) {
        root.add(f.match().field("placeType.identifier").matching(placeTypeIdentifier));
      }

      if (openAt != null) {
        // porządnie przetestować 

        SearchPredicate baseTimePredicate = f.and(
            f.nested("standardOpeningTimes")
                .add(f.range().field("standardOpeningTimes.startTime").atMost(openAt.toIntTime()))
                .add(f.range().field("standardOpeningTimes.endTime").greaterThan(openAt.toIntTime()))
        ).toPredicate();

        SearchPredicate incrementedTimePredicate = f.and(
            f.nested("standardOpeningTimes")
                .add(f.range().field("standardOpeningTimes.startTime").atMost(openAt.toIncrementedIntTime()))
                .add(f.range().field("standardOpeningTimes.endTime").greaterThan(openAt.toIncrementedIntTime()))
        ).toPredicate();

        root.add(f.or(baseTimePredicate, incrementedTimePredicate));
      }

      if (location != null) {
        root.add(f.spatial().within().field("location").circle(location.getLocation(),
            location.getRadius()));
      }

    });

    List<PlaceDb> hits = query.fetch(page * size, size).hits();
    List<PlaceSearchDto> list = hits.stream().map(placeDtoMapper::toDto).toList();

    for (var element : hits) {
      log.info("Place: {}, \nexplanation: \n{}\nplace: \n{}", element.getName(),
          query.toQuery().extension(ElasticsearchExtension.get()).explain(element.getId()), element);
    }

    return list;
  }
}
