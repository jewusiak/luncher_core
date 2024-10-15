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
}
