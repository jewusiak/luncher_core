package pl.luncher.v3.luncher_core.place.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OpeningWindow {

  private WeekDayTime startTime;
  private WeekDayTime endTime;
}
