package pl.luncher.v3.luncher_core.common.model.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.model.dto.LocationWithRadius;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceSearchRequest {

  private String textQuery;
  private String placeTypeIdentifier;
  private LocalTime openAt;
  // 1 - Monday, 7 - Sunday
  @Min(1)
  @Max(7)
  private Integer dayOfWeek;
  private LocationWithRadius location;

  @NotNull
  private Integer page;
  @NotNull
  private Integer size;
}
