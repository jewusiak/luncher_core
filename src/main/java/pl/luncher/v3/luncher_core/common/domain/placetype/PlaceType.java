package pl.luncher.v3.luncher_core.common.domain.placetype;

import pl.luncher.v3.luncher_core.common.model.dto.PlaceTypeDto;

public interface PlaceType {

  void save();

  PlaceTypeDto castToDto();
}
