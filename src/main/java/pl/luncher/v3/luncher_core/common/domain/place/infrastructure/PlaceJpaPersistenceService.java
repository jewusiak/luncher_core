package pl.luncher.v3.luncher_core.common.domain.place.infrastructure;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.common.domain.place.domain.Place;
import pl.luncher.v3.luncher_core.common.domain.place.domain.PlacePersistenceService;
import pl.luncher.v3.luncher_core.common.persistence.infrastructure.PlaceDbMapper;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb;
import pl.luncher.v3.luncher_core.common.persistence.models.UserDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceRepository;
import pl.luncher.v3.luncher_core.common.persistence.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class PlaceJpaPersistenceService implements PlacePersistenceService {

  private final PlaceRepository placeRepository;
  private final PlaceDbMapper placeDbMapper;
  private final UserRepository userRepository;


  @Override
  public Place getById(UUID placeId) {
    PlaceDb placeDb = placeRepository.findById(placeId).orElseThrow();
    Place place = new Place(placeDbMapper.toDto(placeDb));
    return place;
  }

  @Override
  public void save(Place place) {
    UserDb user = null;
    if (place.getPlaceDto().getOwner() != null) {
      user = userRepository.findUserByEmail(place.getPlaceDto().getOwner().getEmail())
          .orElseThrow();
    }
    var placeDb = placeDbMapper.toEntity(place.getPlaceDto(), user);

    placeRepository.save(placeDb);
  }

  @Override
  public void delete(UUID placeId) {
    placeRepository.deleteById(placeId);
  }
}
