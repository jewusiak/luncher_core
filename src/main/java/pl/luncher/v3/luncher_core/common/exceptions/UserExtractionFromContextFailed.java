package pl.luncher.v3.luncher_core.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserExtractionFromContextFailed extends RuntimeException {

  public UserExtractionFromContextFailed() {
    super("User can't be extracted from context!");
  }
}
