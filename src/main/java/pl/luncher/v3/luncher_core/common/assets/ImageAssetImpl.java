package pl.luncher.v3.luncher_core.common.assets;

import com.google.cloud.storage.Storage;
import pl.luncher.v3.luncher_core.common.persistence.models.ImageAssetDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.AssetRepository;
import pl.luncher.v3.luncher_core.common.persistence.repositories.GcpAssetRepository;


class ImageAssetImpl extends GcpBlobAsset {

  private ImageAssetDb imageAssetDb;

  private final AssetRepository assetRepository;

  /**
   * Creates and initializes new asset
   */
  public ImageAssetImpl(GcpAssetRepository gcpAssetRepository, Storage storage,
      AssetRepository assetRepository, String bucketName, String pathPrefix,
      MimeContentFileType filetype, String inputName, String inputDescription) {
    super(gcpAssetRepository, storage, bucketName, pathPrefix, filetype);
    this.assetRepository = assetRepository;
    imageAssetDb = ImageAssetDb.builder().name(inputName).description(inputDescription).build();
    super.connectToBlob(imageAssetDb::setBlobAsset);
  }

  @Override
  public void save() {
    super.save();
    imageAssetDb = assetRepository.save(imageAssetDb);
  }

}
