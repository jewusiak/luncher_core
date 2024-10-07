package pl.luncher.v3.luncher_core.controllers.dtos.common.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pl.luncher.v3.luncher_core.controllers.dtos.common.OpeningWindowDto;
import pl.luncher.v3.luncher_core.place.model.OpeningWindow;

@Mapper(componentModel = ComponentModel.SPRING)
public interface OpeningWindowDtoMapper {

  OpeningWindow toEntity(OpeningWindowDto openingWindowDto);

  OpeningWindowDto toDto(OpeningWindow openingWindow);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  OpeningWindow partialUpdate(
      OpeningWindowDto openingWindowDto, @MappingTarget OpeningWindow openingWindow);
}
