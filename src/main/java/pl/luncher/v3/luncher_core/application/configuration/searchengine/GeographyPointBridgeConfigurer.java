package pl.luncher.v3.luncher_core.application.configuration.searchengine;

import org.hibernate.search.engine.spatial.GeoPoint;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class GeographyPointBridgeConfigurer implements ValueBridge<Point, GeoPoint> {

  @Override
  public GeoPoint toIndexedValue(Point value, ValueBridgeToIndexedValueContext context) {
    return value == null ? null : GeoPoint.of(value.getY(), value.getX());
  }

  @Override
  public Point fromIndexedValue(GeoPoint value, ValueBridgeFromIndexedValueContext context) {
    return value == null ? null
        : new GeometryFactory().createPoint(new Coordinate(value.longitude(), value.latitude()));
  }
}
