package pl.luncher.v3.luncher_core.presentation.controllers.dtos.common;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.place.model.OpeningWindow}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpeningWindowDto implements Serializable {

  private WeekDayTimeDto startTime;
  private WeekDayTimeDto endTime;
}
