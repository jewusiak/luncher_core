package pl.luncher.v3.luncher_core.common.permissions;

public interface WithAssetPermissionContext {

  PermissionChecker edit();

  PermissionChecker delete();
}
