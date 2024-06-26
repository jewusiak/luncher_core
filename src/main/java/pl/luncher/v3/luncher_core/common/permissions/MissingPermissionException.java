package pl.luncher.v3.luncher_core.common.permissions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MissingPermissionException extends ResponseStatusException {

  public MissingPermissionException(String message) {
    super(HttpStatus.FORBIDDEN, message);
  }

}
