package pl.luncher.v3.luncher_core.controllers.dtos.placetype.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceTypeRequest {

  private String identifier;
  private String iconName;
  private String name;
}
