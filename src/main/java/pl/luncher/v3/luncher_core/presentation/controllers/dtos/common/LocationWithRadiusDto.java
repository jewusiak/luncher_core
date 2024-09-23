package pl.luncher.v3.luncher_core.presentation.controllers.dtos.common;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.place.model.LocationWithRadius}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationWithRadiusDto implements Serializable {

  @NotNull
  private Double latitude;
  @NotNull
  private Double longitude;
  @NotNull
  @Positive
  private Double radius;
}
