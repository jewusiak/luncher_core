package pl.luncher.v3.luncher_core.it.steps;

import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.castMap;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.getLastCreatedPlaceUuid;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.getRASpecificationWithAuthAndAcceptHeaders;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.saveHttpResp;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import pl.luncher.v3.luncher_core.common.model.requests.CreateAssetRequest;
import pl.luncher.v3.luncher_core.common.persistence.models.AssetDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceRepository;

@RequiredArgsConstructor
public class AssetSteps {

  private final PlaceRepository placeRepository;

  @When("User requests creation of a new asset for the latest created place:")
  public void userRequestsCreationOfANewAssetForTheLatestCreatedPlace(
      List<Map<String, String>> data) {
    String newPlaceUuid = getLastCreatedPlaceUuid();
    CreateAssetRequest crr = castMap(data.get(0), CreateAssetRequest.class);
    crr.setPlaceId(newPlaceUuid);

    saveHttpResp(
        getRASpecificationWithAuthAndAcceptHeaders().body(crr).when().post("/asset").thenReturn());
  }

  @And("Last created place has {int} asset(s):")
  public void lastCreatedPlaceHasAsset(int arg0, List<Map<String, String>> assetData) {
    placeRepository.findById(UUID.fromString(getLastCreatedPlaceUuid())).ifPresentOrElse(place -> {

          var expectedAssets = assetData.stream().map(map -> castMap(map, AssetDb.class))
              .toList();
          Assertions.assertThat(place.getImages().size()).isEqualTo(arg0);
          Assertions.assertThat(place.getImages()).usingRecursiveComparison().ignoringCollectionOrder()
              .comparingOnlyFields("name", "description").isEqualTo(expectedAssets);

        },
        () -> {
          throw new EntityNotFoundException();
        });
  }

  @When("User deletes last asset from last created place")
  public void userDeletesLastAssetFromLastCreatedPlace() {
    placeRepository.findById(UUID.fromString(getLastCreatedPlaceUuid())).ifPresentOrElse(place -> {
          var assetId = place.getImages().get(place.getImages().size() - 1).getUuid();

          saveHttpResp(
              getRASpecificationWithAuthAndAcceptHeaders().when().delete("/asset/%s".formatted(assetId))
                  .thenReturn());
        },
        () -> {
          throw new EntityNotFoundException();
        });
  }
}
