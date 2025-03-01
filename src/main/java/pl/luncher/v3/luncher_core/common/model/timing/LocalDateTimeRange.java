package pl.luncher.v3.luncher_core.common.model.timing;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LocalDateTimeRange implements TimeRange {

  private LocalDateTime startTime;
  private LocalDateTime endTime;

  @Override
  public boolean isWithin(LocalDateTime time) {
    //          \/ is before or equal
    return !startTime.isAfter(time) && endTime.isAfter(time);
  }

  @Override
  public LocalDateTime getSoonestOccurrence(LocalDateTime at) {
    return startTime.isBefore(at) ? (endTime.isAfter(at) ? at : endTime.minusSeconds(1))
        : startTime;
  }
}
