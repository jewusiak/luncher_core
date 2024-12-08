package pl.luncher.v3.luncher_core.contentmanagement.domainservices.exceptions;

public class PrimaryArrangementDeletionProhibitedException extends RuntimeException {

  public PrimaryArrangementDeletionProhibitedException(String message) {
    super(message);
  }
}
