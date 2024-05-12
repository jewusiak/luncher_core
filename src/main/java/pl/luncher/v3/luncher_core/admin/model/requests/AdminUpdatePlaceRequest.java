package pl.luncher.v3.luncher_core.admin.model.requests;

import lombok.Value;
import pl.luncher.v3.luncher_core.common.place.valueobject.Address;

@Value
public class AdminUpdatePlaceRequest {

  String name;
  String longName;
  String description;
  String googleMapsPlaceId;
  String facebookPageId;
  String instagramHandle;
  String webpageUrl;
  String phoneNumber;
  Address address;
  String googleMapsReference;
  String placeTypeIdentifier;
}
