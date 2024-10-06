package pl.luncher.v3.luncher_core.controllers.dtos.place.requests;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.controllers.dtos.common.AddressDto;
import pl.luncher.v3.luncher_core.controllers.dtos.common.LocationDto;
import pl.luncher.v3.luncher_core.controllers.dtos.common.OpeningWindowDto;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceCreateRequest {

  private String name;
  private String longName;
  private String description;
  private String facebookPageId;
  private String instagramHandle;
  private String webpageUrl;
  private String phoneNumber;
  private AddressDto address;
  private String googleMapsReference;
  private List<OpeningWindowDto> openingWindows;
  private String placeTypeIdentifier;
  private LocationDto location;

}
