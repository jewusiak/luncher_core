package pl.luncher.v3.luncher_core.common.model.timing;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WeekDayTimeRange implements TimeRange {

  private WeekDayTime startTime;
  private WeekDayTime endTime;


  public boolean isWithin(WeekDayTime time) {
    int startTime = getStartTime().toIntTime();
    int endTime = getStartTime().compareTo(getEndTime()) > 0
        ? getEndTime().toIncrementedIntTime()
        : getEndTime().toIntTime();
    return (startTime <= time.toIntTime() && time.toIntTime() < endTime) ||
        (startTime <= time.toIncrementedIntTime() && time.toIncrementedIntTime() < endTime);
  }

  @Override
  public boolean isWithin(LocalDateTime time) {
    return isWithin(WeekDayTime.of(time));
  }

  @Override
  public LocalDateTime getSoonestOccurrence(LocalDateTime at) {
    if (isWithin(at)) {
      return at;
    }
    var dayOffset = (startTime.getDay().getValue() - at.getDayOfWeek().getValue()) % 7;
    if (dayOffset == 0 && !endTime.getTime().isAfter(at.toLocalTime())) {
      dayOffset = 7;
    }
    var offsetData = at.plusDays(dayOffset);

    return LocalDateTime.of(offsetData.getYear(), offsetData.getMonth(), offsetData.getDayOfMonth(),
        startTime.getTime().getHour(), startTime.getTime().getMinute(),
        startTime.getTime().getSecond());
  }

  @Override
  public LocalDateTimeRange getThisOrNextOccurrence(LocalDateTime at) {
    if (isWithin(at)) {
      return new LocalDateTimeRange(startTime.getPreviousOccurrence(at),
          endTime.getThisOrNextOccurrence(at));
    }
    var start = startTime.getThisOrNextOccurrence(at);
    var end = endTime.getThisOrNextOccurrence(start);
    return new LocalDateTimeRange(start, end);
  }

}
