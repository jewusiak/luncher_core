package pl.luncher.v3.luncher_core.place.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceType {

  private String identifier;
  private String iconName;
  private String name;
}
