package pl.luncher.v3.luncher_core.common.domain.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Value;

@Value
public class PlaceOpeningExceptionDto {

  UUID uuid;
  LocalDate date;
  List<OpeningWindowDto> openingWindows;
  String description;
}
