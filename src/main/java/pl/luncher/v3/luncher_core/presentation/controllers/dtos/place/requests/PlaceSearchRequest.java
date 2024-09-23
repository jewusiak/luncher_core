package pl.luncher.v3.luncher_core.presentation.controllers.dtos.place.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.place.model.LocationWithRadius;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.common.LocationDto;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.common.LocationWithRadiusDto;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.common.WeekDayTimeDto;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.place.domainservices.PlaceSearchService.SearchRequest}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceSearchRequest {

  private String textQuery;

  private String placeTypeIdentifier;

  // day: 0-6, 0 is Monday
  @Valid
  private WeekDayTimeDto openAt;

  @Valid
  private LocationWithRadiusDto location;

  @NotNull
  @PositiveOrZero
  private Integer page;

  @PositiveOrZero
  @NotNull
  private Integer size;
}
