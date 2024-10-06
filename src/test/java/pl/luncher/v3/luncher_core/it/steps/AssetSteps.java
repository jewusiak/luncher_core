package pl.luncher.v3.luncher_core.it.steps;

import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.castMap;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.getIdFromCache;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.givenHttpRequest;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.putIdToCache;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.saveHttpResp;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import pl.luncher.v3.luncher_core.controllers.dtos.assets.requests.CreateAssetRequest;
import pl.luncher.v3.luncher_core.it.steps.ParentSteps.EntityIdType;
import pl.luncher.v3.luncher_core.place.persistence.model.AssetDb;
import pl.luncher.v3.luncher_core.place.persistence.repositories.PlaceRepository;

@RequiredArgsConstructor
public class AssetSteps {

  private final PlaceRepository placeRepository;

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
    placeRepository.findById(UUID.fromString(getIdFromCache(placeIdx, EntityIdType.PLACE)))
        .ifPresentOrElse(place -> {

              var expectedAssets = assetData.stream().map(map -> castMap(map, AssetDb.class))
                  .toList();
              Assertions.assertThat(place.getImages().size()).isEqualTo(assetCount);
              Assertions.assertThat(place.getImages()).usingRecursiveComparison()
                  .ignoringCollectionOrder()
                  .comparingOnlyFields("name", "description").isEqualTo(expectedAssets);

            },
            () -> {
              throw new EntityNotFoundException();
            });
  }

  @When("User deletes Asset ID {}")
  public void userDeletesLastAssetFromLastCreatedPlace(String assetIdx) {
    var assetId = getIdFromCache(assetIdx, EntityIdType.ASSET);

    saveHttpResp(givenHttpRequest().when().delete("/asset/%s".formatted(assetId))
        .thenReturn());

  }
}
