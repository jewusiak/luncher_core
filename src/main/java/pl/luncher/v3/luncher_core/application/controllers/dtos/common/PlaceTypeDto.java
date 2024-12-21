package pl.luncher.v3.luncher_core.application.controllers.dtos.common;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.place.model.PlaceType}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceTypeDto implements Serializable {

  private String identifier;
  private String iconName;
  private String name;
}
