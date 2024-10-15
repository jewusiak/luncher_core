package pl.luncher.v3.luncher_core.controllers.dtos.common.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pl.luncher.v3.luncher_core.common.model.timing.WeekDayTimeRange;
import pl.luncher.v3.luncher_core.controllers.dtos.common.WeekDayTimeRangeDto;

@Mapper(componentModel = ComponentModel.SPRING)
public interface WeekDayTimeRangeDtoMapper {

  WeekDayTimeRange toEntity(WeekDayTimeRangeDto weekDayTimeRangeDto);

  WeekDayTimeRangeDto toDto(WeekDayTimeRange weekDayTimeRange);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  WeekDayTimeRange partialUpdate(
      WeekDayTimeRangeDto weekDayTimeRangeDto, @MappingTarget WeekDayTimeRange weekDayTimeRange);
}
