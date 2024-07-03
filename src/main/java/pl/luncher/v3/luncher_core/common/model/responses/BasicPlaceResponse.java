package pl.luncher.v3.luncher_core.common.model.responses;

import java.util.UUID;
import lombok.Value;
import pl.luncher.v3.luncher_core.common.model.dto.PlaceTypeDto;

@Value
public class BasicPlaceResponse {

  UUID id;
  String name;
  String longName;
  PlaceTypeDto placeType;
}
