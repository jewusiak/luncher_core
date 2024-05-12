package pl.luncher.v3.luncher_core.common.place.valueobject;

import java.io.Serializable;
import lombok.Value;


@Value
public class PlaceTypeDto implements Serializable {

  String identifier;
  String iconName;
  String name;
}