package pl.luncher.v3.luncher_core.place.domainservices;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetManagementService;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.place.model.Place;

@Service
@RequiredArgsConstructor
public class PlaceManagementService {


  private final PlacePersistenceService placePersistenceService;
  private final AssetManagementService assetManagementService;

  public Place updatePlace(Place place, List<UUID> imageIds) {

    if (imageIds != null) {
      var distinctRequestedIds = new HashSet<>(imageIds);
      if (distinctRequestedIds.size() != imageIds.size()) {
        throw new IllegalArgumentException("Image ids from request cannot have duplicates!");
      }

      var placeImagesIds = place.getImages().stream().map(Asset::getId).collect(Collectors.toSet());
      if (!placeImagesIds.containsAll(distinctRequestedIds)) {
        throw new IllegalArgumentException(
            "Requested image set has to be a subset of Place's images!");
      }

      List<Asset> oldImages = place.getImages();

      place.setImages(Optional.ofNullable(place.getImages()).map(List::stream)
          .map(s -> s.filter(a -> imageIds.contains(a.getId())))
          .map(s -> s.sorted(Comparator.comparingInt(item -> imageIds.indexOf(item.getId()))))
          .map(Stream::toList).orElse(null));

      if (oldImages != null) {
        oldImages.stream().filter(asset -> !imageIds.contains(asset.getId()))
            .forEach(assetManagementService::deleteAsset);
      }

    }

    place.validate();
    return placePersistenceService.save(place);
  }
}
