package pl.luncher.v3.luncher_core.application.controllers.dtos.place.responses;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.application.controllers.dtos.common.AddressDto;
import pl.luncher.v3.luncher_core.application.controllers.dtos.common.AssetBasicResponse;
import pl.luncher.v3.luncher_core.application.controllers.dtos.common.LocationDto;
import pl.luncher.v3.luncher_core.application.controllers.dtos.common.PlaceTypeDto;
import pl.luncher.v3.luncher_core.application.controllers.dtos.common.UserDto;
import pl.luncher.v3.luncher_core.application.controllers.dtos.common.WeekDayTimeRangeDto;
import pl.luncher.v3.luncher_core.application.controllers.dtos.menus.dtos.MenuOfferDto;


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
  private List<WeekDayTimeRangeDto> openingWindows;
  private PlaceTypeDto placeType;
  private LocationDto location;
  private UserDto owner;
  private List<AssetBasicResponse> images;
  private List<MenuOfferDto> menuOffers;
  private Boolean enabled;
}
