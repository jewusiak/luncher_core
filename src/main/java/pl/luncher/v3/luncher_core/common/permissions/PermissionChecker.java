package pl.luncher.v3.luncher_core.common.permissions;

public interface PermissionChecker {

  static PermissionChecker of(boolean hasPermission) {
    return () -> hasPermission;
  }

  boolean hasPermission();

  default void throwIfNotPermitted() {
    if (!hasPermission()) {
      throw new MissingPermissionException("Missing permissions!");
    }
  }
}
