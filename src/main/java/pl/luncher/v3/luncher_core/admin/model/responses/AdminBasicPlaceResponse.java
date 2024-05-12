package pl.luncher.v3.luncher_core.admin.model.responses;

import java.util.UUID;
import lombok.Value;
import pl.luncher.v3.luncher_core.common.place.valueobject.PlaceTypeDto;

@Value
public class AdminBasicPlaceResponse {

  UUID id;
  String name;
  String longName;
  PlaceTypeDto placeType;
}
