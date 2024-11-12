package pl.luncher.v3.luncher_core.assets.domainservices;

import java.util.UUID;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.assets.model.AssetUploadStatus;
import pl.luncher.v3.luncher_core.place.model.Place;


public class AssetFactory {

  public static Asset newFilesystemPersistent(String description, UUID placeId) {
    return Asset.builder().description(description).uploadStatus(AssetUploadStatus.AWAITING)
        .placeId(placeId)
        .build();
  }
}
