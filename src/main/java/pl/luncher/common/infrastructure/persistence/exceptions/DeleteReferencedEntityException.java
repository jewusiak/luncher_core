package pl.luncher.common.infrastructure.persistence.exceptions;

public class DeleteReferencedEntityException extends RuntimeException {

  public DeleteReferencedEntityException(String message) {
    super(message);
  }
}
