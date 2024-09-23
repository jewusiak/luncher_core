package pl.luncher.v3.luncher_core.presentation.controllers.dtos.common.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.place.model.LocationWithRadius;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.common.LocationWithRadiusDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface LocationWithRadiusDtoMapper {

  LocationWithRadius toEntity(LocationWithRadiusDto locationWithRadiusDto);

  LocationWithRadiusDto toDto(LocationWithRadius locationWithRadius);
}
