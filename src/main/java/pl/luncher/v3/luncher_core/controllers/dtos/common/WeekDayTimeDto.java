package pl.luncher.v3.luncher_core.controllers.dtos.common;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.model.timing.WeekDayTime;

/**
 * DTO for {@link WeekDayTime}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekDayTimeDto implements Serializable {

  @NotNull
  private LocalTime time;

  @NotNull
  private DayOfWeek day;
}
