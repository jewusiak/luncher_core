package pl.luncher.v3.luncher_core.place.domainservices;

import pl.luncher.v3.luncher_core.common.permissions.PermissionChecker;
import pl.luncher.v3.luncher_core.user.model.User;

public interface PlacePermissionsChecker {

  PlacePermissionsChecker byUser(User user);

  PermissionChecker delete();

  PermissionChecker edit();

  PermissionChecker changeOwner();
}
