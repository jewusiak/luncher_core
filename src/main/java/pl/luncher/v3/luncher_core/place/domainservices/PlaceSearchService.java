package pl.luncher.v3.luncher_core.place.domainservices;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.model.timing.WeekDayTime;
import pl.luncher.v3.luncher_core.place.model.LocationWithRadius;
import pl.luncher.v3.luncher_core.place.model.Place;

public interface PlaceSearchService {

  List<Place> search(@Valid SearchRequest searchRequest);
  
  void reindexDb() throws InterruptedException;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class SearchRequest {

    String textQuery;
    String placeTypeIdentifier;
    WeekDayTime openAt;
    @Valid
    LocationWithRadius location;

    LocalDateTime hasLunchServedAt;


    UUID owner;

    Boolean enabled;

    @NotNull
    Integer page;
    @NotNull
    Integer size;

  }
}
