package pl.luncher.v3.luncher_core.common.permissions;

interface WithAssetPermissionContext {

  PermissionChecker edit();

  PermissionChecker delete();
}
