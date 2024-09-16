package pl.luncher.v3.luncher_core.common.domain.place.domain.dtos;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.common.persistence.models.Address}
 */
@AllArgsConstructor
@Getter
public class AddressDto implements Serializable {

  private final String firstLine;
  private final String secondLine;
  private final String zipCode;
  private final String city;
  private final String district;
  private final String description;
  private final String country;
}