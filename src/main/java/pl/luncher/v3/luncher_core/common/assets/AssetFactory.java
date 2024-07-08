package pl.luncher.v3.luncher_core.common.assets;

import com.google.cloud.storage.Storage;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.common.persistence.models.AssetDb;
import pl.luncher.v3.luncher_core.common.persistence.models.CommonAssetDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.AssetRepository;
import pl.luncher.v3.luncher_core.common.persistence.repositories.GcpAssetRepository;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceRepository;
import pl.luncher.v3.luncher_core.common.properties.GcpObjectStorageProperties;

@Component
@RequiredArgsConstructor
public class AssetFactory {

  private final GcpAssetRepository gcpAssetRepository;
  private final Storage storage;
  private final AssetRepository assetRepository;
  private final GcpObjectStorageProperties gcpObjectStorageProperties;
  private final PlaceRepository placeRepository;

  public Asset pullFromRepo(UUID uuid) {
    AssetDb assetDb = assetRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException(
        "Asset with id %s hasn't been found".formatted(uuid.toString())));

    if (assetDb instanceof CommonAssetDb commonAssetDb) {
      return commonAssetOf(commonAssetDb);
    }

    throw new IllegalStateException("Retrieved asset type is not known!");
  }

  private Asset commonAssetOf(CommonAssetDb commonAssetDb) {
    return new CommonAssetImpl(gcpAssetRepository, storage, assetRepository, placeRepository, commonAssetDb);
  }


  public Asset createCommonAsset(String inputName, String description, String fileExtension) {
    Asset asset = new CommonAssetImpl(gcpAssetRepository, storage, assetRepository, placeRepository,
        gcpObjectStorageProperties.getBucketName(), "",
        MimeContentFileType.fromExtension(fileExtension), inputName, description,
        gcpObjectStorageProperties.getGcpHost());

    asset.save();
    return asset;
  }

}
