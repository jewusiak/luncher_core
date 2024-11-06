package pl.luncher.v3.luncher_core.assets.domainservices.exceptions;

public class CannotEstablishFileTypeException extends RuntimeException {

  public CannotEstablishFileTypeException() {
    super("Cannot establish uploaded file's extension!");
  }
}
