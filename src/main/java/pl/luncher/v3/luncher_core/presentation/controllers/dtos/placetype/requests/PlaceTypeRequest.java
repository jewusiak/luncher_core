package pl.luncher.v3.luncher_core.presentation.controllers.dtos.placetype.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
