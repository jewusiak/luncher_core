package pl.luncher.v3.luncher_core.place.persistence;

import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.place.domainservices.PlaceSearchService;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.place.persistence.model.PlaceDb;
import pl.luncher.v3.luncher_core.place.persistence.model.mappers.PlaceDbMapper;

@Slf4j
@Service
@RequiredArgsConstructor
class HibernateSearchPlaceSearchService implements PlaceSearchService {

  private final PlaceDbMapper placeDbMapper;
  private final EntityManager entityManager;

  @Override
  public List<Place> search(@Valid SearchRequest searchRequest) {

    var query = Search.session(entityManager).search(PlaceDb.class).where((f, root) -> {
      root.add(f.matchAll());

      if (searchRequest.getTextQuery() != null) {

        root.add(f.or(f.match().fields("description").matching(searchRequest.getTextQuery())
            /*.fuzzy(1)*/,
            f.match().fields("name", "longName").matching(searchRequest.getTextQuery())));
      }

      if (searchRequest.getPlaceTypeIdentifier() != null) {
        root.add(f.match().field("placeType.identifier")
            .matching(searchRequest.getPlaceTypeIdentifier()));
      }

      if (searchRequest.getOpenAt() != null) {
        // porządnie przetestować

        SearchPredicate baseTimePredicate = f.and(f.nested("openingWindows")
                .add(f.range().field("openingWindows.startTime")
                    .atMost(searchRequest.getOpenAt().toIntTime()))
                .add(f.range().field("openingWindows.endTime")
                    .greaterThan(searchRequest.getOpenAt().toIntTime())))
            .toPredicate();

        SearchPredicate incrementedTimePredicate = f.and(f.nested("openingWindows")
            .add(f.range().field("openingWindows.startTime")
                .atMost(searchRequest.getOpenAt().toIncrementedIntTime()))
            .add(f.range().field("openingWindows.endTime")
                .greaterThan(searchRequest.getOpenAt().toIncrementedIntTime()))).toPredicate();

        root.add(f.or(baseTimePredicate, incrementedTimePredicate));
      }

      if (searchRequest.getLocation() != null) {
        root.add(f.spatial().within().field("location")
            .circle(searchRequest.getLocation().getLatitude(),
                searchRequest.getLocation().getLongitude(),
                searchRequest.getLocation().getRadius()));
      }

    });

    List<PlaceDb> hits = query.fetch(searchRequest.getPage() * searchRequest.getSize(),
        searchRequest.getSize()).hits();
    List<Place> list = hits.stream().map(placeDbMapper::toDomain).toList();

    for (var element : hits) {
      log.info("Place: {}, \nexplanation: \n{}\nplace: \n{}", element.getName(),
          query.toQuery().extension(ElasticsearchExtension.get()).explain(element.getId()),
          element);
    }

    return list;
  }
}
