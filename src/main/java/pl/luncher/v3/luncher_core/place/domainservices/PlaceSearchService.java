package pl.luncher.v3.luncher_core.place.domainservices;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import pl.luncher.v3.luncher_core.place.model.LocationWithRadius;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.place.model.WeekDayTime;

public interface PlaceSearchService {

  List<Place> search(@Valid SearchRequest searchRequest);

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class SearchRequest {

    String textQuery;
    String placeTypeIdentifier;
    WeekDayTime openAt;
    @Valid
    LocationWithRadius location;

    @NotNull
    Integer page;
    @NotNull
    Integer size;
  }
}
