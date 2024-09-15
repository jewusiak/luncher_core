package pl.luncher.v3.luncher_core.common.domain.place;

import lombok.RequiredArgsConstructor;
import pl.luncher.v3.luncher_core.common.domain.place.model.Place;
import pl.luncher.v3.luncher_core.common.domain.users.User;
import pl.luncher.v3.luncher_core.common.permissions.PermissionChecker;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;

@RequiredArgsConstructor
class PlacePermissionsCheckerImpl implements PlacePermissionsChecker {

  private final Place place;
  private User user;

  @Override
  public PlacePermissionsChecker byUser(User user) {
    this.user = user;
    return this;
  }

  @Override
  public PermissionChecker delete() {
    return edit();
  }

  @Override
  public PermissionChecker edit() {
    return () -> {
      if (user.getRole().compareRoleTo(AppRole.SYS_MOD) >= 0) {
        return true;
      }
      return place.getOwner().equals(user);
    };
  }

  @Override
  public PermissionChecker changeOwner() {
    return edit();
  }
}
