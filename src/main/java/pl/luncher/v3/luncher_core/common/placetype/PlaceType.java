package pl.luncher.v3.luncher_core.common.placetype;

import pl.luncher.v3.luncher_core.common.model.dto.PlaceTypeDto;

public interface PlaceType {

  void save();

  PlaceTypeDto castToDto();
}
