package pl.luncher.v3.luncher_core.it.steps;

import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.castMap;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.getIdFromCache;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.givenHttpRequest;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.putIdToCache;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.saveHttpResp;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import pl.luncher.v3.luncher_core.controllers.dtos.assets.requests.CreateAssetRequest;
import pl.luncher.v3.luncher_core.infrastructure.persistence.PlaceRepositoryHelper;
import pl.luncher.v3.luncher_core.it.steps.ParentSteps.EntityIdType;

@RequiredArgsConstructor
public class AssetSteps {

  private final PlaceRepositoryHelper placeRepositoryHelper;

  @When("User requests creation of a new asset for Place ID {} at ID {}:")
  public void userRequestsCreationOfANewAssetForTheLatestCreatedPlace(String placeIdx,
      Integer assetIdx,
      List<Map<String, String>> data) {
    String newPlaceUuid = getIdFromCache(placeIdx, EntityIdType.PLACE);
    CreateAssetRequest crr = castMap(data.get(0), CreateAssetRequest.class);
    crr.setPlaceId(newPlaceUuid);

    Response response = givenHttpRequest().body(crr).when().post("/asset").thenReturn();
    saveHttpResp(response);
    putIdToCache(response.jsonPath().get("assetId"), assetIdx, EntityIdType.ASSET);
  }

  @And("Place ID {} has {int} asset(s):")
  public void lastCreatedPlaceHasAsset(String placeIdx, int assetCount,
      List<Map<String, String>> assetData) {
    placeRepositoryHelper.assertAssetsCount(placeIdx, assetCount, assetData);
  }

  @When("User deletes Asset ID {}")
  public void userDeletesLastAssetFromLastCreatedPlace(String assetIdx) {
    var assetId = getIdFromCache(assetIdx, EntityIdType.ASSET);

    saveHttpResp(givenHttpRequest().when().delete("/asset/%s".formatted(assetId))
        .thenReturn());

  }
}
