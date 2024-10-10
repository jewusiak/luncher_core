package pl.luncher.v3.luncher_core.user.domainservices;

import lombok.RequiredArgsConstructor;
import pl.luncher.v3.luncher_core.common.permissions.PermissionChecker;
import pl.luncher.v3.luncher_core.user.model.AppRole;
import pl.luncher.v3.luncher_core.user.model.User;

@RequiredArgsConstructor
public class UserPermissionsChecker {

  private final User editedUser;
  private User requestingUser;

  public UserPermissionsChecker byUser(User requestingUser) {
    this.requestingUser = requestingUser;
    return this;
  }

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

  public PermissionChecker edit() {
    return this::isHigherThanModAndEditedUserOrHighestRole;
  }

  public PermissionChecker createThisUser() {
    return this::isHigherThanModAndEditedUserOrHighestRole;
  }
}
