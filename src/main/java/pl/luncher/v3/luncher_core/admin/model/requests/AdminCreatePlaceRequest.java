package pl.luncher.v3.luncher_core.admin.model.requests;

import lombok.Value;
import pl.luncher.v3.luncher_core.common.domain.dtos.PlaceOpeningDetailsDto;
import pl.luncher.v3.luncher_core.common.domain.dtos.PlaceTypeDto;
import pl.luncher.v3.luncher_core.common.domain.valueobjects.Address;

import java.util.Set;
import java.util.UUID;

@Value
public class AdminCreatePlaceRequest {
    String name;
    String longName;
    String description;
    Address address;
    String googleMapsReference;

    PlaceOpeningDetailsDto openingDetails;

    UUID ownerUuid;

    PlaceTypeDto placeType;

    Set<UUID> allowedUsersUuids;
}
