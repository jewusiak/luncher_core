package pl.luncher.v3.luncher_core.application.controllers.dtos.placetype.requests;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlaceTypeRequest {

  @Size(min = 1)
  private String iconName;
  @Size(min = 1)
  private String name;
}
