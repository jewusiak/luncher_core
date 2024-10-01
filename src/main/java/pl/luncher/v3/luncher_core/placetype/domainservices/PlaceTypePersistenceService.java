package pl.luncher.v3.luncher_core.placetype.domainservices;

import java.util.List;
import pl.luncher.v3.luncher_core.placetype.model.PlaceType;

public interface PlaceTypePersistenceService {

  PlaceType getByIdentifier(String identifier);

  List<PlaceType> getAll();

  PlaceType save(PlaceType placeType);

  void deleteByIdentifier(String identifier);
}
