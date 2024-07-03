package pl.luncher.v3.luncher_core.common.place;

import com.google.api.gax.rpc.UnimplementedException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminPlaceCreationRequest;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.model.requests.CreatePlaceRequest;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceTypeDb;

@Component
@RequiredArgsConstructor
public class PlaceFactory {

  private final PlacePersistenceService placeEntityPersistenceService;
  private final PlaceDbMapper placeDbMapper;


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

  public Place of(AdminPlaceCreationRequest request) {
    PlaceTypeDb placeTypeDb = placeEntityPersistenceService.getOptionalPlaceType(
        request.getPlaceTypeIdentifier()).orElse(null);

    // todo: remove
    throw new UnsupportedOperationException();
//    return of(placeDbMapper.fromCreation(request, placeTypeDb));
  }

  public Place of(CreatePlaceRequest request, User user) {
    PlaceTypeDb placeTypeDb = placeEntityPersistenceService.getOptionalPlaceType(
        request.getPlaceTypeIdentifier()).orElseThrow();

    PlaceDb placeDb = placeDbMapper.fromCreation(request, placeTypeDb, user);

    return of(placeDb);
  }

  private Place of(PlaceDb placeDb) {
    return new PlaceImpl(placeDb, placeEntityPersistenceService, placeDbMapper);
  }
}
