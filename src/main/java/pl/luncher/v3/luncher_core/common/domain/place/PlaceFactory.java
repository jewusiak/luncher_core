package pl.luncher.v3.luncher_core.common.domain.place;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.common.domain.users.User;
import pl.luncher.v3.luncher_core.common.domain.users.UserFactory;
import pl.luncher.v3.luncher_core.common.model.requests.PlaceCreateRequest;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceTypeDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceTypeRepository;

@Component
@RequiredArgsConstructor
public class PlaceFactory {

  private final PlacePersistenceService placeEntityPersistenceService;
  private final PlaceDbMapper placeDbMapper;
  private final PlaceTypeRepository placeTypeRepository;
  private final UserFactory userFactory;


  public Place pullFromRepo(UUID placeId) {
    Optional<PlaceDb> fromRepo = placeEntityPersistenceService.getFromRepo(placeId);

    if (fromRepo.isEmpty()) {
      throw new EntityNotFoundException("Place with id %s hasn't been found".formatted(placeId));
    }

    return of(fromRepo.get());
  }

  public List<Place> pullFromRepo(int size, int page) {
    return placeEntityPersistenceService.getFromRepo(size, page).stream().map(this::of).toList();
  }

  public List<Place> pullFromRepo(String query, int size, int page) {
    // todo: implement text search
    return List.of();
  }

  public Place of(PlaceCreateRequest request, User user) {
    PlaceDb placeDb = placeDbMapper.fromCreation(request, user, placeTypeRepository);

    return of(placeDb);
  }

  public Place of(PlaceDb placeDb) {
    return new PlaceImpl(placeDb, placeEntityPersistenceService, placeDbMapper, placeTypeRepository, userFactory);
  }
}
