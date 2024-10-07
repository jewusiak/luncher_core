package pl.luncher.v3.luncher_core.common.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.locationtech.jts.geom.Point;
import pl.luncher.v3.luncher_core.place.persistence.model.LocationDb;

@Converter
public class LocationToPointPersistenceConverter implements AttributeConverter<LocationDb, Point> {

  @Override
  public Point convertToDatabaseColumn(LocationDb attribute) {
    return attribute == null ? null : attribute.toPoint();
  }

  @Override
  public LocationDb convertToEntityAttribute(Point dbData) {
    return dbData == null ? null : new LocationDb(dbData.getY(), dbData.getX());
  }
}
