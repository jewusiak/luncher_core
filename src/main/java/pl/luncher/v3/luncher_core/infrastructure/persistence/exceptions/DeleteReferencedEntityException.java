package pl.luncher.v3.luncher_core.infrastructure.persistence.exceptions;

public class DeleteReferencedEntityException extends RuntimeException {

  public DeleteReferencedEntityException(String message) {
    super(message);
  }
}
