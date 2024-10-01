package pl.luncher.v3.luncher_core.user.domainservices;

import lombok.RequiredArgsConstructor;
import pl.luncher.v3.luncher_core.common.permissions.PermissionChecker;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;
import pl.luncher.v3.luncher_core.user.model.User;

@RequiredArgsConstructor
class UserPermissionsCheckerImpl implements UserPermissionsChecker {

  private final User editedUser;
  private User requestingUser;

  @Override
  public UserPermissionsChecker byUser(User requestingUser) {
    this.requestingUser = requestingUser;
    return this;
  }

  @Override
  public PermissionChecker delete() {
    return this::isHigherThanModAndEditedUserOrHighestRole;
  }

  /**
   * user role has to be higher than SYS_MOD and higher than the edited user unless its the
   * highest.
   */
  private boolean isHigherThanModAndEditedUserOrHighestRole() {
    return (requestingUser.getRole().compareRoleTo(AppRole.SYS_MOD) >= 0
        && requestingUser.getRole().compareRoleTo(editedUser.getRole()) > 0)
        || requestingUser.getRole().equals(AppRole.getHighestRole());
  }

  @Override
  public PermissionChecker edit() {
    return this::isHigherThanModAndEditedUserOrHighestRole;
  }

  @Override
  public PermissionChecker createThisUser() {
    return this::isHigherThanModAndEditedUserOrHighestRole;
  }
}
