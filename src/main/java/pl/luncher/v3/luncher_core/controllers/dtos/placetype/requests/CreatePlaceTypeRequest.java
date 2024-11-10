package pl.luncher.v3.luncher_core.controllers.dtos.placetype.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlaceTypeRequest {

  @Size(min = 1)
  private String identifier;
  @NotBlank
  private String iconName;
  @NotBlank
  private String name;
}
