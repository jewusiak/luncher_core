package pl.luncher.v3.luncher_core.placetype.domainservices;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.placetype.model.PlaceType;

@Service
@RequiredArgsConstructor
class PlaceTypeManagementServiceImpl implements PlaceTypeManagementService {

  private final PlaceTypePersistenceService placeTypePersistenceService;

  @Override
  public PlaceType createPlaceType(PlaceType placeType) {
    placeType.validate();
    return placeTypePersistenceService.create(placeType);
  }

  @Override
  public PlaceType updatePlaceType(PlaceType incoming) {
    PlaceType placeType = placeTypePersistenceService.getByIdentifier(incoming.getIdentifier());

    if (incoming.getName() != null) {
      placeType.setName(incoming.getName());
    }
    if (incoming.getIconName() != null) {
      placeType.setIconName(incoming.getIconName());
    }
    placeType.validate();

    return placeTypePersistenceService.update(placeType);
  }

  @Override
  public void deleteByIdentifier(String identifier) {
    placeTypePersistenceService.deleteByIdentifier(identifier);
  }

  @Override
  public List<PlaceType> getAllPlaceTypes() {
    return placeTypePersistenceService.getAll();
  }

  @Override
  public PlaceType getByIdentifier(String identifier) {
    return placeTypePersistenceService.getByIdentifier(identifier);
  }
}
