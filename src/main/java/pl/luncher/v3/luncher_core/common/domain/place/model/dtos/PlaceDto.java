package pl.luncher.v3.luncher_core.common.domain.place.model.dtos;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.luncher.v3.luncher_core.common.model.dto.Location;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb}
 */
@AllArgsConstructor
@Getter
@Setter
public class PlaceDto implements Serializable {

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
  private List<OpeningWindowDto> standardOpeningTimes;
  private PlaceTypeDto placeType;
  private Location location;
  private List<AssetDto> images;
  private UserDto owner;
}