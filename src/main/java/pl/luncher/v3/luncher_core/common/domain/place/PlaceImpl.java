package pl.luncher.v3.luncher_core.common.domain.place;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.luncher.v3.luncher_core.common.domain.users.User;
import pl.luncher.v3.luncher_core.common.domain.users.UserFactory;
import pl.luncher.v3.luncher_core.common.model.dto.OpeningWindowDto;
import pl.luncher.v3.luncher_core.common.model.requests.PlaceUpdateRequest;
import pl.luncher.v3.luncher_core.common.model.responses.BasicPlaceResponse;
import pl.luncher.v3.luncher_core.common.model.responses.FullPlaceResponse;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceTypeRepository;

@AllArgsConstructor
@Getter(AccessLevel.PACKAGE)
class PlaceImpl implements Place {

  private PlaceDb placeDb;

  private final PlacePersistenceService placeEntityPersistenceService;
  private final PlaceDbMapper placeDbMapper;
  private final PlaceTypeRepository placeTypeRepository;
  private final UserFactory userFactory;

  @Override
  public void save() {
    placeDb = placeEntityPersistenceService.persist(placeDb);
  }

  @Override
  public void updateWith(PlaceUpdateRequest request) {
    placeDbMapper.updateWith(placeDb, request, placeTypeRepository);
  }

  @Override
  public BasicPlaceResponse castToBasicPlaceResponse() {
    return placeDbMapper.mapToBasic(placeDb);
  }

  @Override
  public FullPlaceResponse castToFullPlaceResponse() {
    return placeDbMapper.mapToFull(placeDb);
  }

  @Override
  public void addOpeningWindow(OpeningWindowDto openingWindow) {
//    var openingWindowDb = placeDbMapper.mapToDb(openingWindow);
//    openingWindowDb.setPlace(this.placeDb);
//
//    placeDb.addStandardOpeningTime(openingWindowDb);
  }

  @Override
  public void removeOpeningWindow(UUID openingWindowId) {
//    if (placeDb.getStandardOpeningTimes() == null) {
//      return;
//    }
//    placeDb.getStandardOpeningTimes()
//        .removeIf(openingWindowEntity -> openingWindowEntity.getUuid().equals(openingWindowId));
  }

  @Override
  public UUID getPlaceId() {
    return placeDb.getId();
  }

  @Override
  public PlacePermissionsChecker permissions() {
    return new PlacePermissionsCheckerImpl(this);
  }

  @Override
  public PlaceDb getDbEntity() {
    return placeDb;
  }

  @Override
  public void changeOwner(User newOwner) {
    placeDb.setOwner(newOwner.getDbEntity());
  }

  @Override
  public User getOwner() {
    return userFactory.of(placeDb.getOwner());
  }
}
