package pl.luncher.v3.luncher_core.common.place;

import java.util.UUID;
import lombok.AllArgsConstructor;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminUpdatePlaceRequest;
import pl.luncher.v3.luncher_core.admin.model.responses.AdminBasicPlaceResponse;
import pl.luncher.v3.luncher_core.admin.model.responses.AdminFullPlaceResponse;
import pl.luncher.v3.luncher_core.common.assets.AssetToPlaceConnector;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb;
import pl.luncher.v3.luncher_core.common.place.valueobject.OpeningWindowDto;

@AllArgsConstructor
class PlaceImpl implements Place {

  private PlaceDb placeDb;
  private final PlacePersistenceService placeEntityPersistenceService;
  private final PlaceDbMapper placeDbMapper;


  @Override
  public void save() {
    placeDb = placeEntityPersistenceService.persist(placeDb);
  }

  @Override
  public void updateWith(AdminUpdatePlaceRequest request) {
    var placeTypeEntity = placeEntityPersistenceService.getOptionalPlaceType(
        request.getPlaceTypeIdentifier()).orElse(null);

    placeDbMapper.updateWith(placeDb, request, placeTypeEntity);
  }

  @Override
  public AdminBasicPlaceResponse castToAdminBasicPlaceResponse() {
    return placeDbMapper.mapToBasic(placeDb);
  }

  @Override
  public AdminFullPlaceResponse castToAdminFullPlaceResponse() {
    return placeDbMapper.mapToFull(placeDb);
  }

  @Override
  public void addOpeningWindow(OpeningWindowDto openingWindow) {
    var openingWindowDb = placeDbMapper.mapToDb(openingWindow);
    openingWindowDb.setPlace(this.placeDb);

    placeDb.addStandardOpeningTime(openingWindowDb);
  }

  @Override
  public void removeOpeningWindow(UUID openingWindowId) {
    if (placeDb.getStandardOpeningTimes() == null) {
      return;
    }
    placeDb.getStandardOpeningTimes()
        .removeIf(openingWindowEntity -> openingWindowEntity.getUuid().equals(openingWindowId));
  }

  @Override
  public UUID getPlaceId() {
    return placeDb.getId();
  }

  @Override
  public AssetToPlaceConnector getAssetToPlaceConnectorWithRef() {
    return () -> placeDb;
  }


}
