package pl.luncher.v3.luncher_core.application.configuration;

public class UserExtractionFromContextFailedException extends RuntimeException {

  public UserExtractionFromContextFailedException() {
    super("User can't be extracted from context!");
  }
}
