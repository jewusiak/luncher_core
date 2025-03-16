package pl.luncher.common.infrastructure.persistence.exceptions;

public class DuplicateEntityException extends RuntimeException {

  public DuplicateEntityException(String message) {
    super(message);
  }
}
