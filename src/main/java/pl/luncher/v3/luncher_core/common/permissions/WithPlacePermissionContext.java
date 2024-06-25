package pl.luncher.v3.luncher_core.common.permissions;

public interface WithPlacePermissionContext {

  PermissionChecker edit();

  PermissionChecker delete();
}
