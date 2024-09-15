package pl.luncher.v3.luncher_core.common.model.responses;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.model.dto.Location;
import pl.luncher.v3.luncher_core.common.model.dto.OpeningWindowDto;
import pl.luncher.v3.luncher_core.common.model.dto.PlaceTypeDto;
import pl.luncher.v3.luncher_core.common.persistence.models.Address;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullPlaceResponse {

  private UUID id;
  private String name;
  private String longName;
  private String description;
  private Address address;
  private String googleMapsReference;
  private String facebookPageId;
  private String instagramHandle;
  private String webpageUrl;
  private String phoneNumber;
  private List<OpeningWindowDto> standardOpeningTimes;
  private Location location;
  private PlaceTypeDto placeType;
}
