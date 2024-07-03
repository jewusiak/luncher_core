package pl.luncher.v3.luncher_core.common.persistence;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceTypeDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceRepository;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceTypeRepository;
import pl.luncher.v3.luncher_core.common.place.PlacePersistenceService;

@Service
@RequiredArgsConstructor
public class DbPlacePersistenceService implements PlacePersistenceService {

  private final PlaceRepository placeRepository;
  private final PlaceTypeRepository placeTypeRepository;

  @Override
  public PlaceDb persist(PlaceDb placeDb) {
    return placeRepository.save(placeDb);
  }

  @Override
  public Optional<PlaceDb> getFromRepo(UUID placeId) {
    return placeRepository.findById(placeId);
  }

  @Override
  public Collection<PlaceDb> getFromRepo(int size, int page) {
    return placeRepository.findAll(PageRequest.of(page, size)).getContent();
  }

  @Override
  public Optional<PlaceTypeDb> getOptionalPlaceType(String placeTypeIdentifier) {
    return Optional.ofNullable(placeTypeIdentifier)
        .flatMap(e -> placeTypeRepository.findById(placeTypeIdentifier));
  }

  @Override
  public void deleteById(UUID id) {
    placeRepository.deleteById(id);
  }

}
