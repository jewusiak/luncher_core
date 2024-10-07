package pl.luncher.v3.luncher_core.common.exceptions;

public class DuplicateEntityException extends RuntimeException {

  public DuplicateEntityException(String message) {
    super(message);
  }
}
