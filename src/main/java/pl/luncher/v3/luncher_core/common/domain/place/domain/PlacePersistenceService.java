package pl.luncher.v3.luncher_core.common.domain.place.domain;

import java.util.UUID;

public interface PlacePersistenceService {

  Place getById(UUID placeId);

  void save(Place place);

  void delete(UUID placeId);

}
