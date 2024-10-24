package pl.luncher.v3.luncher_core.infrastructure.persistence.exceptions;

public class DuplicateEntityException extends RuntimeException {

  public DuplicateEntityException(String message) {
    super(message);
  }
}
