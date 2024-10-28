package pl.luncher.v3.luncher_core.controllers.dtos.place.requests;

import jakarta.validation.constraints.Email;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.controllers.dtos.common.AddressDto;
import pl.luncher.v3.luncher_core.controllers.dtos.common.LocationDto;
import pl.luncher.v3.luncher_core.controllers.dtos.common.WeekDayTimeRangeDto;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.place.model.Place}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceUpdateRequest implements Serializable {

  private UUID id;
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
  private String placeTypeIdentifier;
  private LocationDto location;
  @Email
  private String ownerEmail;
  private Boolean enabled;
}
