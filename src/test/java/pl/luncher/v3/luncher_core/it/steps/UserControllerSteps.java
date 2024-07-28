package pl.luncher.v3.luncher_core.it.steps;

import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.EntityIdType;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.castMap;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.getIdFromCache;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.givenAuthenticated;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.saveHttpResp;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import pl.luncher.v3.luncher_core.common.controllers.errorhandling.model.ErrorResponse;
import pl.luncher.v3.luncher_core.common.model.requests.UserCreateRequest;
import pl.luncher.v3.luncher_core.common.model.requests.UserUpdateRequest;
import pl.luncher.v3.luncher_core.common.model.responses.FullUserDataResponse;
import pl.luncher.v3.luncher_core.common.persistence.repositories.UserRepository;

@RequiredArgsConstructor
public class UserControllerSteps {

  private final UserRepository userRepository;

  @When("User is created as below with ID {int}:")
  public void userIsCreatedAsBelowWithID(int arg0, List<Map<String, String>> data) {
    var request = ParentSteps.castMap(data.get(0), UserCreateRequest.class);

    var resp = ParentSteps.givenAuthenticated().body(request).when().post("users").thenReturn();

    ParentSteps.saveHttpResp(resp);
    try {
      ParentSteps.putIdToCache(resp.jsonPath().getString("uuid"), arg0, EntityIdType.USER);
    } catch (Exception ignored) {
    }
  }

  @Then("GET User with ID {} is as below:")
  public void userWithIDIsAsBelow(String idx, List<Map<String, String>> data) {
    String id = getIdFromCache(idx, EntityIdType.USER);

    var resp = givenAuthenticated().when().get("users/%s".formatted(id)).thenReturn();

    saveHttpResp(resp);

    var expected = ParentSteps.castMapWithErrorHandling(data.get(0), FullUserDataResponse.class, resp.getStatusCode());
    var actual = getResponseBody(resp, FullUserDataResponse.class);

    Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder()
        .ignoringExpectedNullFields()
        .isEqualTo(expected);
  }

  private static Object getResponseBody(Response resp, Class<?> tClass) {
    if (resp.getStatusCode() < 300 && resp.getStatusCode() >= 200) {
      return resp.as(tClass);
    }
    return resp.as(ErrorResponse.class);
  }

  @When("User with ID {} is edited as below:")
  public void userWithIDIsEditedAsBelow(String idx, List<Map<String, String>> data) {
    String id = getIdFromCache(idx, EntityIdType.USER);
    var req = castMap(data.get(0), UserUpdateRequest.class);

    var resp = givenAuthenticated().body(req).when().put("/users/%s".formatted(id)).thenReturn();

    saveHttpResp(resp);
  }

  @When("User with ID {} is deleted")
  public void userWithIDIsDeleted(String idx) {
    String id = getIdFromCache(idx, EntityIdType.USER);

    var resp = givenAuthenticated().when().delete("/users/%s".formatted(id)).thenReturn();

    saveHttpResp(resp);
  }
}
