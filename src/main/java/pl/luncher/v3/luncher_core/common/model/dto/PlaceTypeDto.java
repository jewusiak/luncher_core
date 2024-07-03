package pl.luncher.v3.luncher_core.common.model.dto;

import java.io.Serializable;
import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class PlaceTypeDto implements Serializable {

  String identifier;
  String iconName;
  String name;
}
