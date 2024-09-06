package pl.luncher.v3.luncher_core.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationWithRadius {

  private double latitude;
  private double longitude;
  // in meters
  private double radius;
}
