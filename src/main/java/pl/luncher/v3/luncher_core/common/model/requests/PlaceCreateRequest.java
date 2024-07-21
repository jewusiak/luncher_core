package pl.luncher.v3.luncher_core.common.model.requests;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.domain.place.valueobject.Address;
import pl.luncher.v3.luncher_core.common.model.dto.OpeningWindowDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceCreateRequest {

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

  private List<OpeningWindowDto> standardOpeningTimes;
}
