package pl.luncher.v3.luncher_core.place.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceTypeDto {

  private String identifier;

  private String iconName;

  private String name;
}
