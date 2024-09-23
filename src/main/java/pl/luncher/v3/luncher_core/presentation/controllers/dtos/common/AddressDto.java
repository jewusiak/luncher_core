package pl.luncher.v3.luncher_core.presentation.controllers.dtos.common;

import java.io.Serializable;
import lombok.Value;

@Value
public class AddressDto implements Serializable {

  String firstLine;
  String secondLine;
  String zipCode;
  String city;
  String district;
  String description;
  String country;
}
