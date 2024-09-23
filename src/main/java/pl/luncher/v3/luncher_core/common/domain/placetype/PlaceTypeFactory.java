package pl.luncher.v3.luncher_core.common.domain.placetype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceTypeDb;
import pl.luncher.v3.luncher_core.place.persistence.repositories.PlaceTypeRepository;

@Service
@RequiredArgsConstructor
public class PlaceTypeFactory {

  private final PlaceTypeRepository placeTypeRepository;

  public PlaceType of(String identifier, String iconName, String name) {
    return of(new PlaceTypeDb(identifier, iconName, name));
  }

  private PlaceType of(PlaceTypeDb placeTypeDb) {
    return new PlaceTypeImpl(placeTypeDb, placeTypeRepository);
  }

}
