package pl.luncher.v3.luncher_core.common.model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import pl.luncher.v3.luncher_core.common.place.valueobject.Address;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlaceRequest {
  private String name;
  private String longName;
  private String description;
  private String googleMapsPlaceId;
  private String facebookPageId;
  private String instagramHandle;
  private String webpageUrl;
  private String phoneNumber;
  private Address address;
  private String googleMapsReference;
  private String placeTypeIdentifier;

//  private Map<String, OpeningWindowDto> standardOpenTimes;
}
