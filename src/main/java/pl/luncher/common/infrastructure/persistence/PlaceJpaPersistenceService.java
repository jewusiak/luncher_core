package pl.luncher.common.infrastructure.persistence;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.place.domainservices.PlacePersistenceService;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.placetype.model.PlaceType;
import pl.luncher.v3.luncher_core.user.model.User;

@Service
@RequiredArgsConstructor
class PlaceJpaPersistenceService implements PlacePersistenceService {

  private final PlaceDbMapper placeDbMapper;

  private final PlaceRepository placeRepository;

  private final PlaceTypeRepository placeTypeRepository;
  private final UserRepository userRepository;

  @Override
  public Place save(Place place) {

    PlaceTypeDb placeType = Optional.ofNullable(place.getPlaceType())
        .map(PlaceType::getIdentifier).map(id -> placeTypeRepository.findByIdentifierIgnoreCase(id)
            .orElseThrow(() -> new NoSuchElementException(
                "Place type with identifier %s not found!".formatted(id))))
        .orElse(null);

    UserDb owner = Optional.ofNullable(place.getOwner()).map(User::getUuid)
        .map(id -> userRepository.findUserByUuid(id).orElseThrow(
            () -> new NoSuchElementException("User with ID %s not found!".formatted(id))))
        .orElse(null);

    PlaceDb placeDb = placeDbMapper.toDb(place, owner, placeType);

    if (placeDb.getImages() != null) {
      for (int i = 0; i < placeDb.getImages().size(); i++) {
        placeDb.getImages().get(i).setPlaceImageIdx(i);
      }
    }

    placeDb.setPlaceType(placeType);
    placeDb.setOwner(owner);

    PlaceDb saved = placeRepository.saveAndFlush(placeDb);

    return placeDbMapper.toDomain(saved);
  }

  @Override
  public Place getById(UUID uuid) {
    var placeDb = placeRepository.findById(uuid).orElseThrow(
        () -> new NoSuchElementException("Place with ID %s not found!".formatted(uuid)));
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
