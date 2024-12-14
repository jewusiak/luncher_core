package pl.luncher.v3.luncher_core.controllers.dtos.contentmanagement.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.contentmanagement.model.PageArrangement;
import pl.luncher.v3.luncher_core.contentmanagement.model.SectionElement;
import pl.luncher.v3.luncher_core.controllers.dtos.assets.mappers.AssetDtoMapper;
import pl.luncher.v3.luncher_core.controllers.dtos.contentmanagement.dtos.PageArrangementDto;
import pl.luncher.v3.luncher_core.controllers.dtos.contentmanagement.dtos.SectionElementDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING, uses = {
    AssetDtoMapper.class})
public interface PageArrangementDtoMapper {

  PageArrangement toDomain(PageArrangementDto pageArrangementDto);

  PageArrangementDto toDto(PageArrangement pageArrangement);

  SectionElementDto toDto(SectionElement sectionElement);

  @Mapping(target = "thumbnail", expression = "java(sectionElementDto.getThumbnail() == null ? null : new pl.luncher.v3.luncher_core.assets.model.Asset(){{setId(sectionElementDto.getThumbnail().getId());}})")
  SectionElement toDomain(SectionElementDto sectionElementDto);
}
