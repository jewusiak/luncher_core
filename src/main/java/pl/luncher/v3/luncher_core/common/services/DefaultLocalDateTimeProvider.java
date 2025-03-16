package pl.luncher.v3.luncher_core.common.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.common.interfaces.LocalDateTimeProvider;

@Component
class DefaultLocalDateTimeProvider implements LocalDateTimeProvider {


  @Override
  public LocalDateTime now() {
    return LocalDateTime.now();
  }

  @Override
  public LocalDateTime now(ZoneId zoneId) {
    if (zoneId == null) {
      return now();
    }
    return LocalDateTime.now(zoneId);
  }

  @Override
  public LocalDateTime utc() {
    return LocalDateTime.now(ZoneOffset.UTC);
  }
}
