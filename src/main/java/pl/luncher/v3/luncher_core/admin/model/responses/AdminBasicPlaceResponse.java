package pl.luncher.v3.luncher_core.admin.model.responses;

import lombok.Value;
import pl.luncher.v3.luncher_core.common.domain.dtos.PlaceTypeDto;

import java.util.UUID;

@Value
public class AdminBasicPlaceResponse {
    UUID id;
    String name;
    String longName;
    PlaceTypeDto placeType;
}
