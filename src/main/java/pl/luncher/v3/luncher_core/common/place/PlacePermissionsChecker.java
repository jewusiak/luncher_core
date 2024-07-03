package pl.luncher.v3.luncher_core.common.place;

import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.permissions.PermissionChecker;

public interface PlacePermissionsChecker {

  PlacePermissionsChecker byUser(User user);

  PermissionChecker delete();

  PermissionChecker edit();
}
