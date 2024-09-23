package pl.luncher.v3.luncher_core.presentation.controllers.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

  private double latitude;
  private double longitude;
}
