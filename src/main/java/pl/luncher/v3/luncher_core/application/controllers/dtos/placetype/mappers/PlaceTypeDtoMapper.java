package pl.luncher.v3.luncher_core.application.controllers.dtos.placetype.mappers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.NullValueMappingStrategy;
import pl.luncher.v3.luncher_core.application.controllers.dtos.placetype.FullPlaceTypeResponse;
import pl.luncher.v3.luncher_core.application.controllers.dtos.placetype.requests.CreatePlaceTypeRequest;
import pl.luncher.v3.luncher_core.application.controllers.dtos.placetype.requests.UpdatePlaceTypeRequest;
import pl.luncher.v3.luncher_core.placetype.model.PlaceType;

@Mapper(componentModel = ComponentModel.SPRING, nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface PlaceTypeDtoMapper {

  PlaceType toDomain(CreatePlaceTypeRequest createPlaceTypeRequest);

  PlaceType toDomain(@Valid UpdatePlaceTypeRequest request, @NotBlank String identifier);

  FullPlaceTypeResponse toFullPlaceTypeResponse(PlaceType placeType);
}
