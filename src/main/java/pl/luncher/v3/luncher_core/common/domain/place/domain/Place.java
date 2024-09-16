package pl.luncher.v3.luncher_core.common.domain.place.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.luncher.v3.luncher_core.common.domain.place.domain.dtos.PlaceDto;
import pl.luncher.v3.luncher_core.common.domain.place.domain.dtos.UserDto;
import pl.luncher.v3.luncher_core.common.domain.users.User;

@AllArgsConstructor
@Getter(AccessLevel.PACKAGE)
public class Place {

  @Getter
  private final PlaceDto placeDto;

  public void changeOwner(User newOwner) {
    placeDto.setOwner(UserDto.builder().uuid(newOwner.getUuid()).build());
  }

}
