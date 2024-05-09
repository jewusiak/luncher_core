package pl.luncher.v3.luncher_core.common.domain.dtos;

import java.util.List;
import lombok.Value;

@Value
public class PlaceOpeningDetailsDto {

  List<OpeningWindowDto> standardOpeningTimes;

  List<PlaceOpeningExceptionDto> openingExceptions;
}
