package pl.luncher.v3.luncher_core.it.steps;

import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.castMap;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.getIdFromCache;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.givenAuthenticated;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.putIdToCache;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.saveHttpResp;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.xNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import pl.luncher.v3.luncher_core.common.model.requests.PlaceCreateRequest;
import pl.luncher.v3.luncher_core.common.model.requests.PlaceOwnerUpdateRequest;
import pl.luncher.v3.luncher_core.common.model.requests.PlaceUpdateRequest;
import pl.luncher.v3.luncher_core.common.model.responses.FullPlaceResponse;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceTypeDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceRepository;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceTypeRepository;
import pl.luncher.v3.luncher_core.it.steps.ParentSteps.EntityIdType;

@RequiredArgsConstructor
public class PlaceSteps {

  private final ObjectMapper objectMapper;
  private final PlaceTypeRepository placeTypeRepository;
  private final PlaceRepository placeRepository;

  @When("User creates a place as below ID {}:")
  public void userCreatesAPlaceAsBelow(Integer idx, List<Map<String, String>> data) {
    var req = castMap(data.get(0), PlaceCreateRequest.class);
    Response response = givenAuthenticated().body(req).when().post("/place").thenReturn();
    saveHttpResp(response);
    putIdToCache(response.jsonPath().get("id"), idx, EntityIdType.PLACE);
  }

  @Then("GET place with ID {} is as below:")
  public void retrievedPlaceWithLastCreatedUUIDIsAsBelow(String idx, List<Map<String, String>> data) {
    String id = getIdFromCache(idx, EntityIdType.PLACE);

    Response response = givenAuthenticated().when().get("/place/%s".formatted(id)).thenReturn();
    saveHttpResp(response);

    var expected = castMap(data.get(0), FullPlaceResponse.class);
    var actual = response.as(FullPlaceResponse.class);

    Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().ignoringExpectedNullFields()
        .isEqualTo(expected);
  }

  @And("Place deletion with ID {} results in {int} code")
  public void placeDeletionWithLastCreatedUUIDResultsInCode(String idx, int code) {
    String id = getIdFromCache(idx, EntityIdType.PLACE);

    givenAuthenticated().when().delete("/place/%s".formatted(id)).then().statusCode(code);

  }

  @And("place types exist:")
  public void placeTypesExist(List<Map<String, String>> data) {
    var l = data.stream().map(item -> castMap(item, PlaceTypeDb.class)).toList();
    placeTypeRepository.saveAll(l);
  }

  @And("Place ID {} is as below:")
  public void lastCreatedPlaceIsAsBelow(String idx, List<Map<String, String>> data) {
    var columns = data.get(0);
    String id = getIdFromCache(idx, EntityIdType.PLACE);

    var place = placeRepository.findById(UUID.fromString(id)).orElseThrow();

    Assertions.assertThat(place.getOwner()).isNotNull();
    Assertions.assertThat(place.getOwner())
        .hasFieldOrPropertyWithValue("email", xNull(columns.get("owner.email"), String.class));
  }

  @And("Updates Place with ID {} with data below:")
  public void updatesPlaceWithIDWithDataBelow(String idx, List<Map<String, String>> data) {
    var placeId = getIdFromCache(idx, EntityIdType.PLACE);

    var req = castMap(data.get(0), PlaceUpdateRequest.class);

    Response response = givenAuthenticated().body(req).when().put("/place/%s".formatted(placeId)).thenReturn();
    saveHttpResp(response);

  }

  @When("Request to change owner for Place ID {} is sent:")
  public void requestToChangeOwnerToRmanagerLuncherCorpForPlaceIDIsSent(String placeIdx,
      List<Map<String, String>> data) {
    var placeId = getIdFromCache(placeIdx, EntityIdType.PLACE);

    var req = castMap(data.get(0), PlaceOwnerUpdateRequest.class);

    Response response = givenAuthenticated().body(req).when().put("place/%s/owner".formatted(placeId)).thenReturn();
    saveHttpResp(response);
  }
}
