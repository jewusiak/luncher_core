package pl.luncher.v3.luncher_core.common.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.common.interfaces.LocalDateTimeProvider;

@Component
@Primary
@RequiredArgsConstructor
public class LocalDateTimeProviderMock implements LocalDateTimeProvider {

  private static LocalDateTime returnableDateTime;
  private final DefaultLocalDateTimeProvider localDateTimeProvider;

  public static void resetMock() {
    returnableDateTime = null;
  }

  public static void mockTime(LocalDateTime time) {
    returnableDateTime = time;
  }

  @Override
  public LocalDateTime now() {
    return returnableDateTime == null ? localDateTimeProvider.now() : returnableDateTime;
  }

  @Override
  public LocalDateTime now(ZoneId zoneId) {
    if (zoneId == null) {
      return returnableDateTime == null ? localDateTimeProvider.now() : returnableDateTime;
    }
    return returnableDateTime == null ? localDateTimeProvider.now(zoneId) : returnableDateTime;
  }

  @Override
  public LocalDateTime utc() {
    return returnableDateTime == null ? localDateTimeProvider.utc() : returnableDateTime;
  }
}