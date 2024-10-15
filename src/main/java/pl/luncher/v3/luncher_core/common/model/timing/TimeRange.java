package pl.luncher.v3.luncher_core.common.model.timing;

import java.time.LocalDateTime;

public interface TimeRange {

  boolean isWithin(LocalDateTime time);
}
