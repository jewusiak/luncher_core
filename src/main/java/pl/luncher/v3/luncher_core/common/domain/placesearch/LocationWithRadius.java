package pl.luncher.v3.luncher_core.common.domain.placesearch;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.luncher.v3.luncher_core.common.model.dto.Location;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class LocationWithRadius {

  private final Location location;
  private final double radius;

  static LocationWithRadius of(Double lat, Double lon, Double radius) {
    if (lat == null && lon == null && radius == null) {
      return null;
    }

    Objects.requireNonNull(lat, "lat must not be null");
    Objects.requireNonNull(lon, "lon must not be null");
    Objects.requireNonNull(radius, "radius must not be null");

    return new LocationWithRadius(new Location(lat, lon),
        radius);
  }
}
