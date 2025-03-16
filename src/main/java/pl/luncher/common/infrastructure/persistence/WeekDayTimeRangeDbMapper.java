package pl.luncher.common.infrastructure.persistence;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import pl.luncher.v3.luncher_core.common.model.timing.WeekDayTime;
import pl.luncher.v3.luncher_core.common.model.timing.WeekDayTimeRange;

@Mapper(componentModel = ComponentModel.SPRING)
interface WeekDayTimeRangeDbMapper {


  default WeekDayTimeRangeDb toDbEntity(WeekDayTimeRange weekDayTimeRange) {
    if (weekDayTimeRange == null) {
      return null;
    }

    int endTime = weekDayTimeRange.getStartTime().compareTo(weekDayTimeRange.getEndTime()) > 0
        ? weekDayTimeRange.getEndTime().toIncrementedIntTime()
        : weekDayTimeRange.getEndTime().toIntTime();

    return WeekDayTimeRangeDb.builder()
        .startTime(weekDayTimeRange.getStartTime().toIntTime())
        .endTime(endTime)
        .build();
  }

  default WeekDayTimeRange toDomain(WeekDayTimeRangeDb db) {
    return db == null ? null
        : new WeekDayTimeRange(WeekDayTime.of(db.getStartTime()), WeekDayTime.of(db.getEndTime()));
  }
}
