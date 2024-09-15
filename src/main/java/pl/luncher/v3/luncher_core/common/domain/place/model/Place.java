package pl.luncher.v3.luncher_core.common.domain.place.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.luncher.v3.luncher_core.common.domain.place.model.dtos.PlaceDto;
import pl.luncher.v3.luncher_core.common.domain.place.model.dtos.UserDto;
import pl.luncher.v3.luncher_core.common.domain.users.User;

@AllArgsConstructor
@Getter(AccessLevel.PACKAGE)
public class Place {

  private final PlaceDto placeDto;

  public void changeOwner(User newOwner) {
    placeDto.setOwner(UserDto.builder().uuid(newOwner.getUuid()).build());
  }

}
