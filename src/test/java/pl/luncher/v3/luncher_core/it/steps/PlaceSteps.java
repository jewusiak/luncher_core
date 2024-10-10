package pl.luncher.v3.luncher_core.it.steps;

import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.castMap;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.getIdFromCache;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.givenHttpRequest;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.putIdToCache;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.saveHttpResp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import pl.luncher.v3.luncher_core.controllers.dtos.place.requests.PlaceCreateRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.place.requests.PlaceOwnerUpdateRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.place.requests.PlaceSearchRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.place.requests.PlaceUpdateRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.place.responses.PlaceFullResponse;
import pl.luncher.v3.luncher_core.infrastructure.persistence.PlaceRepositoryHelper;
import pl.luncher.v3.luncher_core.it.steps.ParentSteps.EntityIdType;

@Slf4j
@RequiredArgsConstructor
public class PlaceSteps {

  private final PlaceRepositoryHelper placeRepositoryHelper;

  @When("User creates a place as below ID {}:")
  public void userCreatesAPlaceAsBelow(Integer idx, List<Map<String, String>> data) {
    var req = castMap(data.get(0), PlaceCreateRequest.class);
    Response response = givenHttpRequest().body(req).when().post("/place").thenReturn();
    saveHttpResp(response);
    putIdToCache(response.jsonPath().get("id"), idx, EntityIdType.PLACE);
  }

  @Then("GET place with ID {} is as below:")
  public void retrievedPlaceWithLastCreatedUUIDIsAsBelow(String idx,
      List<Map<String, String>> data) {
    String id = getIdFromCache(idx, EntityIdType.PLACE);

    Response response = givenHttpRequest().when().get("/place/%s".formatted(id)).thenReturn();
    saveHttpResp(response);

    var expected = castMap(data.get(0), PlaceFullResponse.class);
    var actual = response.as(PlaceFullResponse.class);

    Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder()
        .ignoringExpectedNullFields()
        .isEqualTo(expected);
  }

  @And("Place deletion with ID {} results in {int} code")
  public void placeDeletionWithLastCreatedUUIDResultsInCode(String idx, int code) {
    String id = getIdFromCache(idx, EntityIdType.PLACE);

    givenHttpRequest().when().delete("/place/%s".formatted(id)).then().statusCode(code);

  }

  @And("place types exist:")
  public void placeTypesExist(List<Map<String, String>> data) {
    placeRepositoryHelper.savePlaceTypes(data);
  }

  @And("Place ID {} is as below:")
  public void lastCreatedPlaceIsAsBelow(String idx, List<Map<String, String>> data) {
    var columns = data.get(0);
    String id = getIdFromCache(idx, EntityIdType.PLACE);

    placeRepositoryHelper.assertPlaceOwner(id, columns);
  }

  @And("Updates Place with ID {} with data below:")
  public void updatesPlaceWithIDWithDataBelow(String idx, List<Map<String, String>> data) {
    var placeId = getIdFromCache(idx, EntityIdType.PLACE);

    var req = castMap(data.get(0), PlaceUpdateRequest.class);

    Response response = givenHttpRequest().body(req).when().put("/place/%s".formatted(placeId))
        .thenReturn();
    saveHttpResp(response);

  }

  @When("Request to change owner for Place ID {} is sent:")
  public void requestToChangeOwnerToRmanagerLuncherCorpForPlaceIDIsSent(String placeIdx,
      List<Map<String, String>> data) {
    var placeId = getIdFromCache(placeIdx, EntityIdType.PLACE);

    var req = castMap(data.get(0), PlaceOwnerUpdateRequest.class);

    Response response = givenHttpRequest().body(req).when().put("place/%s/owner".formatted(placeId))
        .thenReturn();
    saveHttpResp(response);
  }

  @When("Place Search request is sent as below:")
  public void placeSearchRequestIsSentAsBelow(List<Map<String, String>> data) {
    var request = ParentSteps.castMap(data.get(0), PlaceSearchRequest.class);

    Response response = givenHttpRequest().body(request).when().post("/place/search").thenReturn();

    saveHttpResp(response);
  }
}
