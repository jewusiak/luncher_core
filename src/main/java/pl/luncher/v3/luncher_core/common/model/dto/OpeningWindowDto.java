package pl.luncher.v3.luncher_core.common.model.dto;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.luncher.v3.luncher_core.common.model.valueobjects.WeekDayTime;

@AllArgsConstructor
@Getter
public class OpeningWindowDto implements Serializable {

  private final UUID uuid;
  private final DayOfWeek dayOfWeek;
  private final WeekDayTime startTime;
  private final WeekDayTime endTime;
}
