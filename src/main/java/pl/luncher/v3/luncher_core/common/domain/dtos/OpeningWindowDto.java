package pl.luncher.v3.luncher_core.common.domain.dtos;

import lombok.Value;

import java.time.LocalTime;

@Value
public class OpeningWindowDto {
    int dayOfWeek; // 1-based Monday starting - ISO 8601
    LocalTime start;
    LocalTime end;
    String description;
}
