package pl.luncher.v3.luncher_core.place.domainservices;

import lombok.RequiredArgsConstructor;
import pl.luncher.v3.luncher_core.common.permissions.PermissionChecker;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.user.model.AppRole;
import pl.luncher.v3.luncher_core.user.model.User;

@RequiredArgsConstructor
public class PlacePermissionsCheckerImpl implements PlacePermissionsChecker {

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

      return place.getOwner().getUuid().equals(user.getUuid());
    };
  }

  @Override
  public PermissionChecker changeOwner() {
    return edit();
  }
}
