package pl.luncher.v3.luncher_core.it.steps;

import static org.hamcrest.Matchers.equalTo;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.requests.LoginRequest;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.requests.NewPasswordRequest;

@RequiredArgsConstructor
public class AuthControllerSteps {

  @And("User logs in using credentials:")
  public void userLogsInUsingCredentials(List<Map<String, String>> list) {
    String email = list.get(0).get("email");
    String pass = list.get(0).get("password");
    Response response = RestAssured.given()
        .body(LoginRequest.builder().email(email).password(pass).build())
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .when().post("/auth/login")
        .thenReturn();

    ParentSteps.saveLoginResp(response);
  }

  @Then("User is logged in as {}")
  public void userIsLoggedInAsTestLuncherCorp(String email) {
    ParentSteps.givenHttpRequest()
        .get("/profile")
        .then().body("email", equalTo(email)).statusCode(200);
  }

  @When("User requests password reset for user {}")
  public void userRequestsPasswordResetForUserUserLuncherCorp(String email) {
    Response response = ParentSteps.givenHttpRequest().when().post("auth/requestreset/%s".formatted(email))
        .thenReturn();
    ParentSteps.saveHttpResp(response);
    var resetUri = ParentSteps.getCachedHttpResp().getBody().jsonPath().getString("resetUri");
    ParentSteps.putToCache("resetUri", resetUri);
  }

  @And("User logs out \\(by removing saved auth token)")
  public void removedSavedAuthenticationToken() {
    ParentSteps.removeAuthorizationToken();
  }

  @When("User changes password using last received url to {}")
  public void userChangesPasswordUsingLastReceivedUrlTo(String pass) {
    var resetUri = ParentSteps.getFromCache("resetUri", String.class);
    Response response = ParentSteps.givenHttpRequest().body(new NewPasswordRequest(pass)).when().put(resetUri)
        .thenReturn();
    ParentSteps.saveHttpResp(response);
  }
}
