package pl.luncher.common.infrastructure.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.engine.spatial.GeoPoint;
import org.hibernate.search.mapper.pojo.bridge.builtin.annotation.Latitude;
import org.hibernate.search.mapper.pojo.bridge.builtin.annotation.Longitude;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class LocationDb implements GeoPoint {

  @Latitude
  private double latitude;
  @Longitude
  private double longitude;

  @Override
  public double latitude() {
    return latitude;
  }

  @Override
  public double longitude() {
    return longitude;
  }

  public Point toPoint() {
    return new GeometryFactory().createPoint(new Coordinate(longitude, latitude));
  }
}
