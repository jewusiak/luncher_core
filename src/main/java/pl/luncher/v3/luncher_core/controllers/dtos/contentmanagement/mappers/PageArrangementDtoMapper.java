package pl.luncher.v3.luncher_core.controllers.dtos.contentmanagement.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.contentmanagement.model.PageArrangement;
import pl.luncher.v3.luncher_core.controllers.dtos.contentmanagement.dtos.PageArrangementDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface PageArrangementDtoMapper {

  PageArrangement toDomain(PageArrangementDto pageArrangementDto);

  PageArrangementDto toDto(PageArrangement pageArrangement);

}