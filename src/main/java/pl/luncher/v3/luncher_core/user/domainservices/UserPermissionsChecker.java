package pl.luncher.v3.luncher_core.user.domainservices;

import pl.luncher.v3.luncher_core.common.permissions.PermissionChecker;
import pl.luncher.v3.luncher_core.place.model.UserDto;
import pl.luncher.v3.luncher_core.user.model.User;

public interface UserPermissionsChecker {

  UserPermissionsChecker byUser(User user);

  PermissionChecker delete();

  PermissionChecker edit();

  PermissionChecker createThisUser();
}
