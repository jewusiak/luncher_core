package pl.luncher.v3.luncher_core.infrastructure.persistence;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import pl.luncher.v3.luncher_core.place.model.OpeningWindow;
import pl.luncher.v3.luncher_core.place.model.WeekDayTime;

@Mapper(componentModel = ComponentModel.SPRING)
interface OpeningWindowMapper {


  default OpeningWindowDb toDbEntity(OpeningWindow openingWindow) {
    if (openingWindow == null) {
      return null;
    }

    int endTime = openingWindow.getStartTime().compareTo(openingWindow.getEndTime()) > 0
        ? openingWindow.getEndTime().toIncrementedIntTime()
        : openingWindow.getEndTime().toIntTime();

    return OpeningWindowDb.builder()
        .startTime(openingWindow.getStartTime().toIntTime())
        .endTime(endTime)
        .build();
  }

  default OpeningWindow toDomain(OpeningWindowDb db) {
    return db == null ? null
        : new OpeningWindow(WeekDayTime.of(db.getStartTime()), WeekDayTime.of(db.getEndTime()));
  }
}