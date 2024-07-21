package pl.luncher.v3.luncher_core.common.domain.place;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceTypeDb;

public interface PlacePersistenceService {

  PlaceDb persist(PlaceDb placeDb);

  Optional<PlaceDb> getFromRepo(UUID placeId);

  Collection<PlaceDb> getFromRepo(int size, int page);

  Optional<PlaceTypeDb> getOptionalPlaceType(String placeTypeIdentifier);

  void deleteById(UUID id);
}
