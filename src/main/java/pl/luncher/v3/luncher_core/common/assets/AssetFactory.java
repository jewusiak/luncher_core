package pl.luncher.v3.luncher_core.common.assets;

import com.google.cloud.storage.Storage;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.common.persistence.models.AssetDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.AssetRepository;
import pl.luncher.v3.luncher_core.common.properties.GcpObjectStorageProperties;

@Component
@RequiredArgsConstructor
public class AssetFactory {

  private final Storage storage;
  private final AssetRepository assetRepository;
  private final GcpObjectStorageProperties gcpObjectStorageProperties;

  public Asset pullFromRepo(UUID uuid) {
    AssetDb assetDb = assetRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException(
        "Asset with id %s hasn't been found".formatted(uuid.toString())));

    return assetOfDb(assetDb);
  }

  private Asset assetOfDb(AssetDb assetDb) {
    return new ImageAssetImpl(assetDb, storage, assetRepository);
  }

  public Asset createCommonAsset(String inputName, String description, String fileExtension) {
    Asset asset = new ImageAssetImpl(gcpObjectStorageProperties.getBucketName(), "",
        gcpObjectStorageProperties.getGcpHost(), MimeContentFileType.fromExtension(fileExtension),
        inputName, description, storage, assetRepository);

    asset.save();
    return asset;
  }

}
