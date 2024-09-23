package pl.luncher.v3.luncher_core.place.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.place.persistence.model.AddressDb;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

  private String firstLine;
  private String secondLine;
  private String zipCode;
  private String city;
  private String district;
  private String description;
  private String country;
}
