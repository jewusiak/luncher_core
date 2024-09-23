package pl.luncher.v3.luncher_core.place.domainservices;

import java.util.List;
import java.util.UUID;
import pl.luncher.v3.luncher_core.place.model.Place;

public interface PlacePersistenceService {

  Place save(Place place);

  Place getById(UUID uuid);

  void deleteById(UUID uuid);

  List<Place> getAllPaged(int page, int size);
}
