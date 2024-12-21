package pl.luncher.v3.luncher_core.application.controllers.dtos.contentmanagement.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import pl.luncher.v3.luncher_core.application.controllers.dtos.assets.mappers.AssetDtoMapper;
import pl.luncher.v3.luncher_core.application.controllers.dtos.contentmanagement.dtos.PageArrangementDto;
import pl.luncher.v3.luncher_core.application.controllers.dtos.contentmanagement.dtos.SectionElementDto;
import pl.luncher.v3.luncher_core.contentmanagement.model.PageArrangement;
import pl.luncher.v3.luncher_core.contentmanagement.model.SectionElement;
import pl.luncher.v3.luncher_core.place.domainservices.PlacePersistenceService;
import pl.luncher.v3.luncher_core.placetype.domainservices.PlaceTypePersistenceService;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING, uses = {
    AssetDtoMapper.class})
public abstract class PageArrangementDtoMapper {

  @Autowired
  protected PlacePersistenceService placePersistenceService;
  @Autowired
  protected PlaceTypePersistenceService placeTypePersistenceService;

  public abstract PageArrangement toDomain(PageArrangementDto pageArrangementDto);

  public abstract PageArrangementDto toDto(PageArrangement pageArrangement);

  @Mapping(target = "elementType", expression = "java(sectionElement.getElementType())")
  @Mapping(target = "sourceElementId", expression = "java(sectionElement.getSourceElementId())")
  public abstract SectionElementDto toDto(SectionElement sectionElement);

  @Mapping(target = "thumbnail", expression = "java(sectionElementDto.getThumbnail() == null ? null : new pl.luncher.v3.luncher_core.assets.model.Asset(){{setId(sectionElementDto.getThumbnail().getId());}})")
  @Mapping(target = "place", expression = "java(sectionElementDto.getElementType() == pl.luncher.v3.luncher_core.contentmanagement.model.ElementType.PLACE ? placePersistenceService.getById(java.util.UUID.fromString(sectionElementDto.getSourceElementId())) : null)")
  @Mapping(target = "placeType", expression = "java(sectionElementDto.getElementType() == pl.luncher.v3.luncher_core.contentmanagement.model.ElementType.PLACE_TYPE ? placeTypePersistenceService.getByIdentifier(sectionElementDto.getSourceElementId()) : null)")
  public abstract SectionElement toDomain(SectionElementDto sectionElementDto);
}
