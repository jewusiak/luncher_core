package pl.luncher.v3.luncher_core.common.domain.dtos;

import lombok.Value;

import java.util.List;

@Value
public class PlaceOpeningDetailsDto {
    List<OpeningWindowDto> standardOpeningTimes;

    List<PlaceOpeningExceptionDto> openingExceptions;
}
