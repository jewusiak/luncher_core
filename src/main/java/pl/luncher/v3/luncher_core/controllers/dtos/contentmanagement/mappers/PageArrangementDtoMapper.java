package pl.luncher.v3.luncher_core.controllers.dtos.contentmanagement.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.contentmanagement.model.PageArrangement;
import pl.luncher.v3.luncher_core.contentmanagement.model.SectionElement;
import pl.luncher.v3.luncher_core.controllers.dtos.contentmanagement.dtos.PageArrangementDto;
import pl.luncher.v3.luncher_core.controllers.dtos.contentmanagement.dtos.SectionElementDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface PageArrangementDtoMapper {

  PageArrangement toDomain(PageArrangementDto pageArrangementDto);

  PageArrangementDto toDto(PageArrangement pageArrangement);

  @Mapping(source = "thumbnail.accessUrl", target = "thumbnailAccessUrl")
  @Mapping(source = "thumbnail.id", target = "thumbnailId")
  SectionElementDto toDto(SectionElement sectionElement);

  @Mapping(source = "thumbnailId", target = "thumbnail.id")
  SectionElement toDomain(SectionElementDto sectionElementDto);
}
