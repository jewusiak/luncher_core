package pl.luncher.v3.luncher_core.place.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OpeningWindow {

  private WeekDayTime startTime;
  private WeekDayTime endTime;
}
