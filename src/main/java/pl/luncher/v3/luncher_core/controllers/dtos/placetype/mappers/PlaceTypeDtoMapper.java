package pl.luncher.v3.luncher_core.controllers.dtos.placetype.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pl.luncher.v3.luncher_core.controllers.dtos.placetype.FullPlaceTypeResponse;
import pl.luncher.v3.luncher_core.controllers.dtos.placetype.requests.PlaceTypeRequest;
import pl.luncher.v3.luncher_core.placetype.model.PlaceType;

@Mapper(componentModel = ComponentModel.SPRING)
public interface PlaceTypeDtoMapper {

  PlaceType toDomain(PlaceTypeRequest placeTypeRequest);

  @Mapping(target = "identifier", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateDomain(PlaceTypeRequest request, @MappingTarget PlaceType placeType);

  FullPlaceTypeResponse toFullPlaceTypeResponse(PlaceType placeType);
}
