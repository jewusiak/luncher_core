package pl.luncher.v3.luncher_core.place.model.menus;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.luncher.v3.luncher_core.common.model.timing.LocalDateTimeRange;
import pl.luncher.v3.luncher_core.common.model.timing.WeekDayTime;
import pl.luncher.v3.luncher_core.common.model.timing.WeekDayTimeRange;

class MenuOfferTest {


  @ParameterizedTest
  @CsvSource(value = {
      "2025-01-02T10:00:00,2025-01-02T12:00:00,2025-01-02T09:00:00,2025-01-02T10:00:00",
      "2025-01-02T10:00:00,2025-01-02T12:00:00,2025-01-02T10:00:00,2025-01-02T10:00:00",
      "2025-01-02T10:00:00,2025-01-02T12:00:00,2025-01-02T11:00:00,2025-01-02T11:00:00",
      "2025-01-02T10:00:00,2025-01-02T12:00:00,2025-01-02T11:59:59,2025-01-02T11:59:59",
      "2025-01-02T10:00:00,2025-01-02T12:00:00,2025-01-02T12:00:00,null",
      "2025-01-02T10:00:00,2025-01-02T12:00:00,2025-01-02T13:00:00,null",})
  public void soonestTimeLocalDateTime(String time1, String time2, String timeNow,
      String expected) {
    var mo = new MenuOffer();
    mo.setOneTimeServingRanges(
        List.of(new LocalDateTimeRange(LocalDateTime.parse(time1), LocalDateTime.parse(time2))));

    var asst = Assertions.assertThat(mo.getSoonestServingTime(LocalDateTime.parse(timeNow)));

    if (expected.equals("null")) {
      asst.isNull();
    } else {
      asst.isEqualTo(LocalDateTime.parse(expected));
    }
  }

  @ParameterizedTest
  @CsvSource(value = {
      "1;10:00:00,1;12:00:00,2025-01-06T09:00:00,2025-01-06T10:00:00",
      "1;10:00:00,1;12:00:00,2025-01-06T11:00:00,2025-01-06T11:00:00",
      "1;10:00:00,1;12:00:00,2025-01-06T11:59:59,2025-01-06T11:59:59",
      "1;10:00:00,1;12:00:00,2025-01-06T12:00:00,2025-01-13T10:00:00",
      "1;10:00:00,1;12:00:00,2025-01-12T12:00:00,2025-01-13T10:00:00",
      "7;20:00:00,1;02:00:00,2025-01-06T12:00:00,2025-01-12T20:00:00",
      "7;20:00:00,1;02:00:00,2025-01-12T20:00:00,2025-01-12T20:00:00",
      "7;20:00:00,1;02:00:00,2025-01-12T21:00:00,2025-01-12T21:00:00",
      "7;20:00:00,1;02:00:00,2025-01-13T00:00:00,2025-01-13T00:00:00",
      "7;20:00:00,1;02:00:00,2025-01-13T01:00:00,2025-01-13T01:00:00",
      "7;20:00:00,1;02:00:00,2025-01-13T02:00:00,2025-01-19T20:00:00",
  })
  public void soonestTimeWeekDayTime(String time1, String time2, String timeNow, String expected) {
    var mo = new MenuOffer();
    mo.setRecurringServingRanges(List.of(new WeekDayTimeRange(
        WeekDayTime.of(Integer.parseInt(time1.split(";")[0]), LocalTime.parse(time1.split(";")[1])),
        WeekDayTime.of(Integer.parseInt(time2.split(";")[0]),
            LocalTime.parse(time2.split(";")[1])))));

    var asst = Assertions.assertThat(mo.getSoonestServingTime(LocalDateTime.parse(timeNow)));

    if (expected.equals("null")) {
      asst.isNull();
    } else {
      asst.isEqualTo(LocalDateTime.parse(expected));
    }
  }
}