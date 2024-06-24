package pl.luncher.v3.luncher_core.common.assets;

import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.common.persistence.repositories.AssetRepository;
import pl.luncher.v3.luncher_core.common.persistence.repositories.GcpAssetRepository;
import pl.luncher.v3.luncher_core.common.properties.GcpObjectStorageProperties;

@Component
@RequiredArgsConstructor
public class AssetFactory {

  private final GcpAssetRepository gcpAssetRepository;
  private final Storage storage;
  private final AssetRepository assetRepository;
  private final GcpObjectStorageProperties gcpObjectStorageProperties;

//  public Asset pullFromRepo(UUID uuid) {
//    AssetDb assetDb = assetRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException(
//        "Asset with id %s hasn't been found".formatted(uuid.toString())));
//
//    if (assetDb instanceof GcpAssetDb gcpAssetDb) {
//      return imageAssetof(gcpAssetDb);
//    }
//
//    throw new IllegalStateException("Retrieved asset type is not known!");
//  }

  public Asset createImageAsset(String inputName, String description, String fileExtension) {
    Asset asset = new ImageAssetImpl(gcpAssetRepository, storage, assetRepository,
        gcpObjectStorageProperties.getBucketName(), gcpObjectStorageProperties.getImagePathPrefix(),
        MimeContentFileType.fromExtension(fileExtension), inputName, description);

    asset.save();
    return asset;
  }

}
