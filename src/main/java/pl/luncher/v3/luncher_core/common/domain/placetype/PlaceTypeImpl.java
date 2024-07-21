package pl.luncher.v3.luncher_core.common.domain.placetype;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import pl.luncher.v3.luncher_core.common.model.dto.PlaceTypeDto;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceTypeDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceTypeRepository;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
class PlaceTypeImpl implements
    PlaceType {

  private PlaceTypeDb placeTypeDb;

  private final PlaceTypeRepository placeTypeRepository;

  @Override
  public void save() {
    placeTypeDb = placeTypeRepository.save(placeTypeDb);
  }

  @Override
  public PlaceTypeDto castToDto() {
    return PlaceTypeDto.builder().name(placeTypeDb.getName()).iconName(placeTypeDb.getIconName())
        .identifier(placeTypeDb.getIdentifier()).build();
  }
}
