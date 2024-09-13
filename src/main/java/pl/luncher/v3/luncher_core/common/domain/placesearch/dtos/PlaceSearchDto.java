package pl.luncher.v3.luncher_core.common.domain.placesearch.dtos;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.Value;
import pl.luncher.v3.luncher_core.common.model.dto.Location;
import pl.luncher.v3.luncher_core.common.model.dto.OpeningWindowDto;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb}
 */
@Value
public class PlaceSearchDto implements Serializable {

  UUID id;
  String name;
  String longName;
  String description;
  AddressDto address;
  String googleMapsReference;
  PlaceTypeDto placeType;
  Location location;
  List<AssetDto> images;
  OpeningWindowDto nearestWindow;

  /**
   * DTO for {@link pl.luncher.v3.luncher_core.common.domain.place.valueobject.Address}
   */
  @Value
  public static class AddressDto implements Serializable {

    String firstLine;
    String secondLine;
    String zipCode;
    String city;
    String district;
    String description;
    String country;
  }

  /**
   * DTO for {@link pl.luncher.v3.luncher_core.common.persistence.models.PlaceTypeDb}
   */
  @Value
  public static class PlaceTypeDto implements Serializable {

    String identifier;
    String iconName;
    String name;
  }

  /**
   * DTO for {@link pl.luncher.v3.luncher_core.common.persistence.models.AssetDb}
   */
  @Value
  public static class AssetDto implements Serializable {

    UUID uuid;
    String name;
    String description;
    String publicUrl;
  }
}
