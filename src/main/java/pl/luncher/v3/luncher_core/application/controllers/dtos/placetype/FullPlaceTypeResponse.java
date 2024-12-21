package pl.luncher.v3.luncher_core.application.controllers.dtos.placetype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullPlaceTypeResponse {

  private String identifier;

  private String iconName;

  private String name;
}
