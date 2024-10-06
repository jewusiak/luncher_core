package pl.luncher.v3.luncher_core.controllers.dtos.place.responses;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.controllers.dtos.common.AddressDto;
import pl.luncher.v3.luncher_core.controllers.dtos.common.AssetDto;
import pl.luncher.v3.luncher_core.controllers.dtos.common.LocationDto;
import pl.luncher.v3.luncher_core.controllers.dtos.common.OpeningWindowDto;
import pl.luncher.v3.luncher_core.controllers.dtos.common.PlaceTypeDto;
import pl.luncher.v3.luncher_core.controllers.dtos.common.UserDto;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceFullResponse implements Serializable {

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
  private List<OpeningWindowDto> openingWindows;
  private PlaceTypeDto placeType;
  private LocationDto location;
  private UserDto owner;
  private List<AssetDto> images;
}
