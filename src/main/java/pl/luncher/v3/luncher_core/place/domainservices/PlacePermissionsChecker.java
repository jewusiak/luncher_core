package pl.luncher.v3.luncher_core.place.domainservices;

import pl.luncher.v3.luncher_core.common.permissions.PermissionChecker;
import pl.luncher.v3.luncher_core.place.model.UserDto;

public interface PlacePermissionsChecker {

  PlacePermissionsChecker byUser(UserDto user);

  PermissionChecker delete();

  PermissionChecker edit();

  PermissionChecker changeOwner();
}
