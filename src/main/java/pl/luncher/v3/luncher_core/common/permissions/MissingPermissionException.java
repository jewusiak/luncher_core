package pl.luncher.v3.luncher_core.common.permissions;

public class MissingPermissionException extends RuntimeException {

  public MissingPermissionException(String message) {
    super(message);
  }

}
