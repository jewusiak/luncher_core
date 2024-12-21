package pl.luncher.v3.luncher_core.application.controllers.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

  private double latitude;
  private double longitude;
}
