package pl.luncher.v3.luncher_core.common.interfaces;

import java.time.LocalDateTime;
import java.time.ZoneId;

public interface LocalDateTimeProvider {

  LocalDateTime now();

  LocalDateTime now(ZoneId zoneId);

  LocalDateTime utc();
}
