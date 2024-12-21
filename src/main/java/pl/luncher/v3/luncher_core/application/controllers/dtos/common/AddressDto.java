package pl.luncher.v3.luncher_core.application.controllers.dtos.common;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto implements Serializable {

  private String firstLine;
  private String secondLine;
  private String zipCode;
  private String city;
  private String district;
  private String description;
  private String country;
}
