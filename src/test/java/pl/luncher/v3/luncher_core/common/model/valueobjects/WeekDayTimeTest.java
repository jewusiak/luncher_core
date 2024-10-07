package pl.luncher.v3.luncher_core.common.model.valueobjects;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.luncher.v3.luncher_core.place.model.WeekDayTime;

class WeekDayTimeTest {

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

}
