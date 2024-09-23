package pl.luncher.v3.luncher_core.presentation.controllers.dtos.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.place.model.WeekDayTime}
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
