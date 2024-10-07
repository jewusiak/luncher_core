package pl.luncher.v3.luncher_core.placetype.persistence.model;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import pl.luncher.v3.luncher_core.placetype.model.PlaceType;

@Mapper(componentModel = ComponentModel.SPRING)
public interface PlaceTypeDbMapper {

  PlaceTypeDb toDbEntity(PlaceType placetype);

  PlaceType toDomain(PlaceTypeDb placeTypeDb);
}
