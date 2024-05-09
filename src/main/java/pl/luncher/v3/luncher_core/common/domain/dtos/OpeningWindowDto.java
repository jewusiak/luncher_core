package pl.luncher.v3.luncher_core.common.domain.dtos;

import java.time.LocalTime;
import lombok.Value;

@Value
public class OpeningWindowDto {

  int dayOfWeek; // 1-based Monday starting - ISO 8601
  LocalTime start;
  LocalTime end;
  String description;
}
