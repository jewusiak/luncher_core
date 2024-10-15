package pl.luncher.v3.luncher_core.controllers.dtos.common;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.model.timing.WeekDayTimeRange;

/**
 * DTO for {@link WeekDayTimeRange}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekDayTimeRangeDto implements Serializable {

  private WeekDayTimeDto startTime;
  private WeekDayTimeDto endTime;
}
