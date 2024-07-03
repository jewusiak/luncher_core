package pl.luncher.v3.luncher_core.it.steps;

import io.cucumber.java.en.Then;
import org.assertj.core.api.Assertions;

public class ResponseSteps {

  @Then("response code is {int}")
  public void responseCodeIs(int arg0) {
    Assertions.assertThat(ParentSteps.getCachedHttpResp().getStatusCode()).isEqualTo(arg0);
  }
}
