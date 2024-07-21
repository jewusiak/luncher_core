package pl.luncher.v3.luncher_core.common.domain.users;

import pl.luncher.v3.luncher_core.common.permissions.PermissionChecker;

public interface UserPermissionsChecker {

  UserPermissionsChecker byUser(User user);

  PermissionChecker delete();

  PermissionChecker update();

  PermissionChecker createThisUser();
}
