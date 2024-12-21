package pl.luncher.v3.luncher_core.application.controllers.dtos.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.common.model.timing.LocalDateTimeRange}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalDateTimeRangeDto implements Serializable {

  private LocalDateTime startTime;
  private LocalDateTime endTime;
}
