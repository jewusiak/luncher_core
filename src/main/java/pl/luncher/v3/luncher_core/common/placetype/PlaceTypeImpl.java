package pl.luncher.v3.luncher_core.common.placetype;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceTypeDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceTypeRepository;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class PlaceTypeImpl implements
    PlaceType {

  private PlaceTypeDb placeTypeDb;
  private final PlaceTypeRepository placeTypeRepository;

  @Override
  public void save() {
    placeTypeDb = placeTypeRepository.save(placeTypeDb);
  }
}
