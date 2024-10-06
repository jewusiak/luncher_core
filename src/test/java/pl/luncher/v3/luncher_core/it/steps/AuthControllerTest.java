package pl.luncher.v3.luncher_core.it.steps;

import io.cucumber.java.en.Given;
import java.util.List;
import java.util.Map;
import pl.luncher.v3.luncher_core.controllers.dtos.user.requests.UserRegistrationRequest;

public class AuthControllerTest {

  @Given("User registers as below:")
  public void userRegistersAsBelow(List<Map<String, String>> data) {
    var req = ParentSteps.castMap(data.get(0), UserRegistrationRequest.class);

    var resp = ParentSteps.givenHttpRequest().body(req).when().post("auth/register").thenReturn();

    ParentSteps.saveHttpResp(resp);
  }
}
