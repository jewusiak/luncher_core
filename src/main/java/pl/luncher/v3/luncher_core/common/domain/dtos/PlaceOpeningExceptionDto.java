package pl.luncher.v3.luncher_core.common.domain.dtos;

import lombok.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Value
public class PlaceOpeningExceptionDto {
    UUID uuid;
    LocalDate date;
    List<OpeningWindowDto> openingWindows;
    String description;
}
