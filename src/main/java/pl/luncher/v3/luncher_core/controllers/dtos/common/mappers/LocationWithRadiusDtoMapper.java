package pl.luncher.v3.luncher_core.controllers.dtos.common.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.controllers.dtos.common.LocationWithRadiusDto;
import pl.luncher.v3.luncher_core.place.model.LocationWithRadius;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface LocationWithRadiusDtoMapper {

  LocationWithRadius toEntity(LocationWithRadiusDto locationWithRadiusDto);

  LocationWithRadiusDto toDto(LocationWithRadius locationWithRadius);
}
