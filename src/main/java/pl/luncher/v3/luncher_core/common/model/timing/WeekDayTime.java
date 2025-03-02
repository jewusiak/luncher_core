package pl.luncher.v3.luncher_core.common.model.timing;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class WeekDayTime implements Comparable<WeekDayTime> {

  private static final int SECONDS_IN_A_DAY = 86400;
  public static final int SECONDS_IN_A_WEEK = SECONDS_IN_A_DAY * 7;
  private final LocalTime time;
  private final DayOfWeek day;

  public WeekDayTime(LocalTime time, DayOfWeek day) {
    this.time = time;
    this.day = day;
  }

  public static WeekDayTime of(DayOfWeek day, LocalTime time) {
    return new WeekDayTime(time, day);
  }

  public static WeekDayTime of(@Min(1) @Max(7) int day, LocalTime time) {
    return new WeekDayTime(time, DayOfWeek.of(day));
  }

  public static WeekDayTime of(LocalDateTime time) {
    return new WeekDayTime(LocalTime.from(time), time.getDayOfWeek());
  }

  public static WeekDayTime of(Integer time) {
    if (time == null) {
      return null;
    }
    int dow0based = (time % SECONDS_IN_A_WEEK) / SECONDS_IN_A_DAY;

    LocalTime localTime = LocalTime.ofSecondOfDay(time % SECONDS_IN_A_DAY);
    DayOfWeek dayOfWeek = DayOfWeek.of(dow0based + 1);

    return new WeekDayTime(localTime,
        dayOfWeek);
  }

  public int toIntTime() {
    return (day.getValue() - 1) * SECONDS_IN_A_DAY + time.toSecondOfDay();
  }

  public int toIncrementedIntTime() {
    return (day.getValue() - 1) * SECONDS_IN_A_DAY + time.toSecondOfDay() + SECONDS_IN_A_WEEK;
  }

  @Override
  public int compareTo(@NotNull WeekDayTime o) {
    return this.toIntTime() - o.toIntTime();
  }

  public LocalDateTime getThisOrNextOccurrence(LocalDateTime soonestDate) {
    var dayOffset = (this.getDay().getValue() - soonestDate.getDayOfWeek().getValue()) % 7;
    if (dayOffset == 0 && this.getTime().isBefore(soonestDate.toLocalTime())) {
      dayOffset = 7;
    }
    var offsetData = soonestDate.plusDays(dayOffset);

    return LocalDateTime.of(offsetData.getYear(), offsetData.getMonth(), offsetData.getDayOfMonth(),
        this.getTime().getHour(), this.getTime().getMinute(),
        this.getTime().getSecond());
  }

  public LocalDateTime getPreviousOccurrence(LocalDateTime latestDate) {
    var dayOffset = (latestDate.getDayOfWeek().getValue() - this.getDay().getValue()) % 7;

    if (dayOffset == 0 && this.getTime().isAfter(latestDate.toLocalTime())) {
      dayOffset = 7;
    }
    var offsetData = latestDate.minusDays(dayOffset);

    return LocalDateTime.of(offsetData.getYear(), offsetData.getMonth(), offsetData.getDayOfMonth(),
        this.getTime().getHour(), this.getTime().getMinute(),
        this.getTime().getSecond());
  }
}
