package pl.luncher.v3.luncher_core.place.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationWithRadius {

  @NotNull
  private Double latitude;
  @NotNull
  private Double longitude;
  // in meters
  @NotNull
  private Double radius;
}