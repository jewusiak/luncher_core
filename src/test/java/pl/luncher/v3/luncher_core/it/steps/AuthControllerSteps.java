package pl.luncher.v3.luncher_core.it.steps;

import static org.hamcrest.Matchers.equalTo;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import pl.luncher.v3.luncher_core.common.model.requests.LoginRequest;

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
}
