package pl.luncher.v3.luncher_core.common.place;

import lombok.RequiredArgsConstructor;
import pl.luncher.v3.luncher_core.common.domain.infra.AppRole;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.permissions.PermissionChecker;

@RequiredArgsConstructor
class PlacePermissionsCheckerImpl implements PlacePermissionsChecker {

  private final PlaceImpl place;
  private User user;

  @Override
  public PlacePermissionsChecker byUser(User user) {
    this.user = user;
    return this;
  }

  @Override
  public PermissionChecker delete() {
    return () -> {
      if (user.getRole().compareRoleTo(AppRole.SYS_MOD) >= 0) {
        return true;
      }
      return place.getPlaceDb().getOwner().equals(user);
    };
  }

  @Override
  public PermissionChecker edit() {
    return () -> {
      if (user.getRole().compareRoleTo(AppRole.SYS_MOD) >= 0) {
        return true;
      }
      return place.getPlaceDb().getOwner().equals(user);
    };
  }
}
