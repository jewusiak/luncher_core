package pl.luncher.v3.luncher_core.application.controllers.dtos.place.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.application.controllers.dtos.common.LocationWithRadiusDto;
import pl.luncher.v3.luncher_core.application.controllers.dtos.common.WeekDayTimeDto;

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

  LocalDateTime hasLunchServedAt;

  private String ownerEmail;

  private Boolean enabled;

  @NotNull
  @PositiveOrZero
  private Integer page;

  @PositiveOrZero
  @NotNull
  private Integer size;
}
