package pl.luncher.v3.luncher_core.common.domain.dtos;

import lombok.Value;

@Value
public class PlaceTypeDto {
    String identifier;

    String iconName;

    String name;
}
