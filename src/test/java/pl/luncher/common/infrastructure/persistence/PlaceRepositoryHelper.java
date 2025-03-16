package pl.luncher.common.infrastructure.persistence;

import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.castMap;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.getIdFromCache;
import static pl.luncher.v3.luncher_core.it.steps.ParentSteps.xNull;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.it.steps.ParentSteps.EntityIdType;

@RequiredArgsConstructor
@Component
public class PlaceRepositoryHelper {

  private final PlaceTypeRepository placeTypeRepository;
  private final PlaceRepository placeRepository;

  public void savePlaceTypes(List<Map<String, String>> data) {
    var l = data.stream().map(item -> castMap(item, PlaceTypeDb.class)).toList();
    placeTypeRepository.saveAll(l);
  }


  public void assertPlaceOwner(String id, Map<String, String> columns) {
    var place = placeRepository.findById(UUID.fromString(id)).orElseThrow();

    Assertions.assertThat(place.getOwner()).isNotNull();
    Assertions.assertThat(place.getOwner())
        .hasFieldOrPropertyWithValue("email", xNull(columns.get("owner.email"), String.class));
  }


  public void assertAssetsCount(String placeIdx, int assetCount, List<Map<String, String>> assetData) {
    placeRepository.findById(UUID.fromString(getIdFromCache(placeIdx, EntityIdType.PLACE))).ifPresentOrElse(place -> {

      var expectedAssets = assetData.stream().map(map -> castMap(map, AssetDb.class)).toList();
      Assertions.assertThat(place.getImages().size()).isEqualTo(assetCount);
      Assertions.assertThat(place.getImages()).usingRecursiveComparison().ignoringCollectionOrder()
          .comparingOnlyFields("name", "description").isEqualTo(expectedAssets);

    }, () -> {
      throw new EntityNotFoundException();
    });
  }


}
