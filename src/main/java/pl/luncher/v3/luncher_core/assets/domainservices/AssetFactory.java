package pl.luncher.v3.luncher_core.assets.domainservices;

import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.assets.model.AssetUploadStatus;
import pl.luncher.v3.luncher_core.place.model.Place;


public class AssetFactory {

  public static Asset newFilesystemPersistent(String description, Place place) {
    return Asset.builder().description(description).uploadStatus(AssetUploadStatus.AWAITING)
        .placeId(place.getId())
        .build();
  }
}
