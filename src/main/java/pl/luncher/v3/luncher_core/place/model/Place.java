package pl.luncher.v3.luncher_core.place.model;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.luncher.v3.luncher_core.place.domainservices.PlacePermissionsChecker;
import pl.luncher.v3.luncher_core.place.domainservices.PlacePermissionsCheckerImpl;
import pl.luncher.v3.luncher_core.place.persistence.model.AddressDb;
import pl.luncher.v3.luncher_core.place.persistence.model.PlaceDb;
import pl.luncher.v3.luncher_core.place.persistence.model.LocationDb;
import pl.luncher.v3.luncher_core.place.persistence.model.OpeningWindowDb;

/**
 * DTO for {@link PlaceDb}
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Place {

  private UUID id;
  private String name;
  private String longName;
  private String description;
  private String facebookPageId;
  private String instagramHandle;
  private String webpageUrl;
  private String phoneNumber;
  private Address address;
  private String googleMapsReference;
  private List<OpeningWindow> openingWindows;
  private PlaceType placeType;
  private Location location;
  private UserDto owner;
  private List<Asset> images;

  public void validate() {
    log.info("Place is being validated...");
  }

  public PlacePermissionsChecker permissions() {
    return new PlacePermissionsCheckerImpl(this);
  }

}
