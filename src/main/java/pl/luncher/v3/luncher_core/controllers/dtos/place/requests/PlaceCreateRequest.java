package pl.luncher.v3.luncher_core.controllers.dtos.place.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.controllers.dtos.common.AddressDto;
import pl.luncher.v3.luncher_core.controllers.dtos.common.LocationDto;
import pl.luncher.v3.luncher_core.controllers.dtos.common.WeekDayTimeRangeDto;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceCreateRequest {

  @NotBlank
  private String name;
  private String longName;
  private String description;
  private String facebookPageId;
  private String instagramHandle;
  private String webpageUrl;
  private String phoneNumber;
  private AddressDto address;
  private String googleMapsReference;
  private List<WeekDayTimeRangeDto> openingWindows;
  @NotNull
  private String placeTypeIdentifier;
  private LocationDto location;
  @NotNull
  private Boolean enabled;

}
