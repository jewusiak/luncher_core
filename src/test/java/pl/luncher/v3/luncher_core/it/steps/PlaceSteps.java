package pl.luncher.v3.luncher_core.it.steps;

import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.castMap;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.getFromCache;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.getRASpecificationWithAuthAndAcceptHeaders;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.putToCache;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.saveHttpResp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import pl.luncher.v3.luncher_core.common.model.requests.CreatePlaceRequest;
import pl.luncher.v3.luncher_core.common.model.responses.FullPlaceResponse;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceTypeDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceTypeRepository;

@RequiredArgsConstructor
public class PlaceSteps {

  private final ObjectMapper objectMapper;
  private final PlaceTypeRepository placeTypeRepository;

  @When("User creates a place as below:")
  public void userCreatesAPlaceAsBelow(List<Map<String, String>> data) {
    var req = castMap(data.get(0), CreatePlaceRequest.class);
    Response response = getRASpecificationWithAuthAndAcceptHeaders().body(req).when().post("/place").thenReturn();
    saveHttpResp(response);
    putToCache("newPlaceUuid", response.jsonPath().get("id"));
  }

  @Then("Retrieved place with last created UUID is as below:")
  public void retrievedPlaceWithLastCreatedUUIDIsAsBelow(List<Map<String, String>> data) {
    String id = getFromCache("newPlaceUuid", String.class);

    Response response = getRASpecificationWithAuthAndAcceptHeaders().when().get("/place/%s".formatted(id)).thenReturn();
    saveHttpResp(response);

    var expected = castMap(data.get(0), FullPlaceResponse.class);
    var actual = response.as(FullPlaceResponse.class);

    Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().ignoringExpectedNullFields()
        .isEqualTo(expected);
  }

  @And("Place deletion with last created UUID results in {int} code")
  public void placeDeletionWithLastCreatedUUIDResultsInCode(int code) {
    String id = getFromCache("newPlaceUuid", String.class);

    getRASpecificationWithAuthAndAcceptHeaders().when().delete("/place/%s".formatted(id)).then().statusCode(code);

  }

  @And("place types exist:")
  public void placeTypesExist(List<Map<String, String>> data) {
    var l = data.stream().map(item -> castMap(item, PlaceTypeDb.class)).toList();
    placeTypeRepository.saveAll(l);
  }
}
