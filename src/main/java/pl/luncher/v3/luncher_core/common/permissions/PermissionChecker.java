package pl.luncher.v3.luncher_core.common.permissions;

public interface PermissionChecker {

  boolean hasPermission();

  default void checkPermission() {
    if (!hasPermission()) {
      throw new MissingPermissionException("Missing permissions!");
    }
  }

  static PermissionChecker of(boolean hasPermission) {
    return () -> hasPermission;
  }
}
