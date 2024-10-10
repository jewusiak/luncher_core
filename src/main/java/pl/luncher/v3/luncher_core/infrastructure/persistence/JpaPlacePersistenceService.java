package pl.luncher.v3.luncher_core.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.place.domainservices.PlacePersistenceService;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.place.model.PlaceTypeDto;
import pl.luncher.v3.luncher_core.place.model.UserDto;

@Service
@RequiredArgsConstructor
class JpaPlacePersistenceService implements PlacePersistenceService {

  private final PlaceDbMapper placeDbMapper;

  private final PlaceRepository placeRepository;

  private final PlaceTypeRepository placeTypeRepository;
  private final UserRepository userRepository;

  @Override
  public Place save(Place place) {

    PlaceTypeDb placeType = Optional.ofNullable(place.getPlaceType())
        .map(PlaceTypeDto::getIdentifier).map(id -> placeTypeRepository.findById(id).orElseThrow())
        .orElse(null);

    UserDb owner = Optional.ofNullable(place.getOwner()).map(UserDto::getEmail)
        .map(email -> userRepository.findUserByEmail(email).orElseThrow()).orElse(null);

    PlaceDb placeDb = placeDbMapper.toDbEntity(place, owner, placeType);

    placeDb.setPlaceType(placeType);
    placeDb.setOwner(owner);

    PlaceDb saved = placeRepository.save(placeDb);

    return placeDbMapper.toDomain(saved);
  }

  @Override
  public Place getById(UUID uuid) {
    var placeDb = placeRepository.findById(uuid).orElseThrow();
    return placeDbMapper.toDomain(placeDb);
  }

  @Override
  public void deleteById(UUID uuid) {
    placeRepository.deleteById(uuid);
  }

  @Override
  public List<Place> getAllPaged(int page, int size) {
    Page<PlaceDb> places = placeRepository.findAll(PageRequest.of(page, size));
    return places.stream().map(placeDbMapper::toDomain).toList();
  }
}
