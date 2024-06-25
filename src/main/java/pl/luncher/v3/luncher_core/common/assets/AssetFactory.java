package pl.luncher.v3.luncher_core.common.assets;

import com.google.cloud.storage.Storage;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.common.persistence.models.AssetDb;
import pl.luncher.v3.luncher_core.common.persistence.models.GcpAssetDb;
import pl.luncher.v3.luncher_core.common.persistence.models.ImageAssetDb;
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

  public Asset pullFromRepo(UUID uuid) {
    AssetDb assetDb = assetRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException(
        "Asset with id %s hasn't been found".formatted(uuid.toString())));

    if (assetDb instanceof ImageAssetDb imageAssetDb) {
      return imageAssetOf(imageAssetDb);
    }

    throw new IllegalStateException("Retrieved asset type is not known!");
  }

  private Asset imageAssetOf(ImageAssetDb imageAssetDb) {
    Asset asset = new ImageAssetImpl(gcpAssetRepository, storage, assetRepository, imageAssetDb);
    return null;
  }


  public Asset createImageAsset(String inputName, String description, String fileExtension) {
    Asset asset = new ImageAssetImpl(gcpAssetRepository, storage, assetRepository,
        gcpObjectStorageProperties.getBucketName(), gcpObjectStorageProperties.getImagePathPrefix(),
        MimeContentFileType.fromExtension(fileExtension), inputName, description,
        gcpObjectStorageProperties.getGcpHost());

    asset.save();
    return asset;
  }

}
