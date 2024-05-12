package pl.luncher.v3.luncher_core.admin.model.responses;

import java.util.List;
import java.util.UUID;
import lombok.Value;
import pl.luncher.v3.luncher_core.common.place.valueobject.Address;
import pl.luncher.v3.luncher_core.common.place.valueobject.OpeningWindowDto;
import pl.luncher.v3.luncher_core.common.place.valueobject.PlaceTypeDto;

@Value
public class AdminFullPlaceResponse {

  UUID id;
  String name;
  String longName;
  String description;
  Address address;
  String googleMapsReference;
  String googleMapsPlaceId;
  String facebookPageId;
  String instagramHandle;
  String webpageUrl;
  String phoneNumber;
  List<OpeningWindowDto> standardOpeningTimes;
  PlaceTypeDto placeType;
}
