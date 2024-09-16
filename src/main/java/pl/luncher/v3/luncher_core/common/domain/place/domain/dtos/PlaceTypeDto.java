package pl.luncher.v3.luncher_core.common.domain.place.domain.dtos;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.common.persistence.models.PlaceTypeDb}
 */
@AllArgsConstructor
@Getter
public class PlaceTypeDto implements Serializable {

  private final String identifier;
  private final String iconName;
  private final String name;
}