package pl.luncher.v3.luncher_core.place.domainservices;

import jakarta.validation.Valid;
import java.util.List;
import pl.luncher.v3.luncher_core.place.model.Place;

public interface PlaceSearchService {

  List<Place> search(@Valid PlaceSearchCommand command);
  
  void reindexDb() throws InterruptedException;

}
