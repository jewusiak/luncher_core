package pl.luncher.v3.luncher_core.common.model.valueobjects;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import pl.luncher.v3.luncher_core.common.model.timing.WeekDayTime;

public class WeekDayTimeTest {

  public static Stream<Arguments> normalDatesSource() {
    return Stream.of(
        Arguments.of(0, LocalTime.of(0, 0, 0), DayOfWeek.MONDAY),
        Arguments.of(10 * 60 * 60, LocalTime.of(10, 0, 0), DayOfWeek.MONDAY),
        Arguments.of(23 * 60 * 60 + 59 * 60 + 59, LocalTime.of(23, 59, 59), DayOfWeek.MONDAY),
        Arguments.of(24 * 60 * 60, LocalTime.of(0, 0, 0), DayOfWeek.TUESDAY),
        Arguments.of(7 * 24 * 60 * 60 + 10 * 60, LocalTime.of(0, 10, 0), DayOfWeek.MONDAY),
        Arguments.of(7 * 24 * 60 * 60 + 10 * 60 + 1, LocalTime.of(0, 10, 1), DayOfWeek.MONDAY)
    );
  }

  @ParameterizedTest
  @MethodSource("normalDatesSource")
  public void normalDates(int input, LocalTime expectedTime, DayOfWeek expectedDayOfWeek) {
    WeekDayTime weekDayTime = WeekDayTime.of(input);
    Assertions.assertThat(weekDayTime).isNotNull();
    Assertions.assertThat(weekDayTime.getTime()).isEqualTo(expectedTime);
    Assertions.assertThat(weekDayTime.getDay()).isEqualTo(expectedDayOfWeek);

  }

  @ParameterizedTest
  @CsvSource(value = {
      "1;10:00:00,2025-01-13T09:00:00,2025-01-06T10:00:00",
      "1;10:00:00,2025-01-06T11:00:00,2025-01-06T10:00:00",
      "1;10:00:00,2025-01-06T10:00:00,2025-01-06T10:00:00",
      "1;10:00:00,2025-01-19T10:00:00,2025-01-13T10:00:00",
      "1;10:00:00,2025-01-13T09:59:59,2025-01-06T10:00:00",
  })
  public void previousOccurrence(String time1, String timeNow, String expected) {
    var wdt = new WeekDayTime(LocalTime.parse(time1.split(";")[1]),
        DayOfWeek.of(Integer.parseInt(time1.split(";")[0])));
    Assertions.assertThat(wdt.getPreviousOccurrence(LocalDateTime.parse(timeNow)))
        .isEqualTo(LocalDateTime.parse(expected));
  }

  @ParameterizedTest
  @CsvSource(value = {
      "1;10:00:00,2025-01-06T09:00:00,2025-01-06T10:00:00",
      "1;10:00:00,2025-01-06T11:00:00,2025-01-13T10:00:00",
      "1;10:00:00,2025-01-06T10:00:00,2025-01-06T10:00:00",
      "1;10:00:00,2025-01-06T09:59:59,2025-01-06T10:00:00",
      "1;10:00:00,2025-01-12T10:00:00,2025-01-13T10:00:00",
      "1;10:00:00,2025-01-06T10:00:01,2025-01-13T10:00:00",
  })
  public void nextOccurrence(String time1, String timeNow, String expected) {
    var wdt = new WeekDayTime(LocalTime.parse(time1.split(";")[1]),
        DayOfWeek.of(Integer.parseInt(time1.split(";")[0])));
    Assertions.assertThat(wdt.getThisOrNextOccurrence(LocalDateTime.parse(timeNow)))
        .isEqualTo(LocalDateTime.parse(expected));
  }
}
