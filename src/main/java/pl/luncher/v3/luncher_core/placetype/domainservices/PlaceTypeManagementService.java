package pl.luncher.v3.luncher_core.placetype.domainservices;

import java.util.List;
import pl.luncher.v3.luncher_core.placetype.model.PlaceType;

public interface PlaceTypeManagementService {

  PlaceType createPlaceType(PlaceType placeType);

  PlaceType updatePlaceType(PlaceType domainObject);

  void deleteByIdentifier(String identifier);

  List<PlaceType> getAllPlaceTypes();

  PlaceType getByIdentifier(String identifier);
}
