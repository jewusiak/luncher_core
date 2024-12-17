package pl.luncher.v3.luncher_core.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.common.model.timing.WeekDayTime;
import pl.luncher.v3.luncher_core.place.domainservices.PlaceSearchCommand;
import pl.luncher.v3.luncher_core.place.domainservices.PlaceSearchService;
import pl.luncher.v3.luncher_core.place.model.Place;

@Slf4j
@Service
@RequiredArgsConstructor
class PlaceHibernateSearchService implements PlaceSearchService {

  private final PlaceDbMapper placeDbMapper;
  private final EntityManager entityManager;

  @Override
  public List<Place> search(@Valid PlaceSearchCommand searchRequest) {

    var query = Search.session(entityManager).search(PlaceDb.class).where((f, root) -> {
      root.add(f.matchAll());

      if (searchRequest.getTextQuery() != null) {

        root.add(f.or(f.match().fields("description").matching(searchRequest.getTextQuery())
            /*.fuzzy(1)*/,
            f.match().fields("name", "longName", "address.firstLine", "address.secondLine",
                "address.city", "address.district").matching(searchRequest.getTextQuery())));
      }

      if (searchRequest.getPlaceTypeIdentifier() != null) {
        root.add(f.match().field("placeType.identifier")
            .matching(searchRequest.getPlaceTypeIdentifier()));
      }

      if (searchRequest.getOpenAt() != null) {
        // porządnie przetestować

        SearchPredicate baseTimePredicate = f.nested("openingWindows")
                .add(f.range().field("openingWindows.startTime")
                    .atMost(searchRequest.getOpenAt().toIntTime()))
                .add(f.range().field("openingWindows.endTime")
                    .greaterThan(searchRequest.getOpenAt().toIntTime()))
            .toPredicate();

        SearchPredicate incrementedTimePredicate = f.nested("openingWindows")
            .add(f.range().field("openingWindows.startTime")
                .atMost(searchRequest.getOpenAt().toIncrementedIntTime()))
            .add(f.range().field("openingWindows.endTime")
                .greaterThan(searchRequest.getOpenAt().toIncrementedIntTime())).toPredicate();

        root.add(f.or(baseTimePredicate, incrementedTimePredicate));
      }

      if (searchRequest.getLocation() != null) {
        root.add(f.spatial().within().field("location")
            .circle(searchRequest.getLocation().getLatitude(),
                searchRequest.getLocation().getLongitude(),
                searchRequest.getLocation().getRadius()));
      }

      if (searchRequest.getHasLunchServedAt() != null) {

        // recurring predicate for base int time
        WeekDayTime lunchtime = WeekDayTime.of(searchRequest.getHasLunchServedAt());
        SearchPredicate baseTimePredicate = f.nested("menuOffers.recurringServingRanges")
            .add(f.range().field("menuOffers.recurringServingRanges.startTime")
                .atMost(lunchtime.toIntTime()))
            .add(f.range().field("menuOffers.recurringServingRanges.endTime")
                .greaterThan(lunchtime.toIntTime()))
            .toPredicate();

        // recurring predicate for incremented int time
        SearchPredicate incrementedTimePredicate = f.nested("menuOffers.recurringServingRanges")
            .add(f.range().field("menuOffers.recurringServingRanges.startTime")
                .atMost(lunchtime.toIncrementedIntTime()))
            .add(f.range().field("menuOffers.recurringServingRanges.endTime")
                .greaterThan(lunchtime.toIncrementedIntTime())).toPredicate();

        // one time time predicate
        SearchPredicate onetimePredicate = f.nested("menuOffers.oneTimeServingRanges")
            .add(f.range().field("menuOffers.oneTimeServingRanges.startTime")
                .atMost(searchRequest.getHasLunchServedAt()))
            .add(f.range().field("menuOffers.oneTimeServingRanges.endTime")
                .greaterThan(searchRequest.getHasLunchServedAt())).toPredicate();

        root.add(f.or(baseTimePredicate, incrementedTimePredicate, onetimePredicate));

      }

      // places owned by sbd
      if (searchRequest.getOwner() != null) {
        root.add(f.match().field("owner.uuid").matching(searchRequest.getOwner().getUuid()));
      }

      // is place enabled
      if (searchRequest.getEnabled() != null) {
        root.add(f.match().field("enabled").matching(searchRequest.getEnabled()));
      }

    });

    List<PlaceDb> hits = query.fetch(searchRequest.getPage() * searchRequest.getSize(),
        searchRequest.getSize()).hits();
    List<Place> list = hits.stream().map(placeDbMapper::toDomain).toList();

//    for (var element : hits) {
//      log.debug("Place: {}, \nexplanation: \n{}\nplace: \n{}\n\n", element.getName(),
//          query.toQuery().extension(ElasticsearchExtension.get()).explain(element.getId()),
//          element);
//    }

    return list;
  }

  @Override
  public void reindexDb() throws InterruptedException {
    log.debug("Starting PlaceDb reindexing");
    Search.session(entityManager).scope(PlaceDb.class).massIndexer().threadsToLoadObjects(4).startAndWait();
    log.debug("PlaceDb reindexing finished");
  }
}
