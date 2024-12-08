package pl.luncher.v3.luncher_core.place.domainservices;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetInfoPersistenceService;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetManagementService;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.place.model.Place;

@Service
@RequiredArgsConstructor
public class PlaceManagementService {


  private final PlacePersistenceService placePersistenceService;
  private final AssetManagementService assetManagementService;
  private final AssetInfoPersistenceService assetInfoPersistenceService;

  public Place updatePlace(Place place, List<UUID> imageIds) {

    if (imageIds != null) {
      var distinctRequestedIds = new HashSet<>(imageIds);
      if (distinctRequestedIds.size() != imageIds.size()) {
        throw new IllegalArgumentException("Image ids from request cannot have duplicates!");
      }

//      var placeImagesIds = place.getImages().stream().map(Asset::getId).collect(Collectors.toSet());
//      if (!placeImagesIds.containsAll(distinctRequestedIds)) {
//        throw new IllegalArgumentException(
//            "Requested image set has to be a subset of Place's images!");
//      }

      List<Asset> oldImages = place.getImages() == null ? new ArrayList<>() : place.getImages();

      List<Asset> newImagesList = fetchNewImages(imageIds, oldImages);
      // add old images which should be retained
      var oldImagesToRetain = oldImages.stream().filter(asset -> imageIds.contains(asset.getId())).toList();

      List<Asset> imagesToSave = new ArrayList<>();
      imagesToSave.addAll(newImagesList);
      imagesToSave.addAll(oldImagesToRetain);

      imagesToSave = imagesToSave.stream().sorted(Comparator.comparingInt(item -> imageIds.indexOf(item.getId())))
          .toList();

      place.setImages(imagesToSave);

      oldImages.stream().filter(asset -> !imageIds.contains(asset.getId()))
          .forEach(assetManagementService::deleteAsset);
    }

    place.validate();
    return placePersistenceService.save(place);
  }

  private List<Asset> fetchNewImages(List<UUID> imageIds, List<Asset> oldImages) {
    var oldImagesIds = oldImages.stream().map(Asset::getId).collect(Collectors.toSet());
    return imageIds.stream().filter(id -> !oldImagesIds.contains(id)).map(assetInfoPersistenceService::getById)
        .toList();
  }
}
