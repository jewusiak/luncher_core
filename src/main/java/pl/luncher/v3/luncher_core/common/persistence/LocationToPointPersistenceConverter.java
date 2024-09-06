package pl.luncher.v3.luncher_core.common.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.locationtech.jts.geom.Point;
import pl.luncher.v3.luncher_core.common.model.dto.Location;

@Converter
public class LocationToPointPersistenceConverter implements AttributeConverter<Location, Point> {

  @Override
  public Point convertToDatabaseColumn(Location attribute) {
    return attribute == null ? null : attribute.toPoint();
  }

  @Override
  public Location convertToEntityAttribute(Point dbData) {
    return dbData == null ? null : new Location(dbData.getY(), dbData.getX());
  }
}
