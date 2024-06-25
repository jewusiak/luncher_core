package pl.luncher.v3.luncher_core.common.assets;

import com.google.cloud.storage.Storage;
import pl.luncher.v3.luncher_core.common.persistence.models.ImageAssetDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.AssetRepository;
import pl.luncher.v3.luncher_core.common.persistence.repositories.GcpAssetRepository;
import pl.luncher.v3.luncher_core.common.place.Place;


class ImageAssetImpl extends GcpBlobAsset {

  private ImageAssetDb imageAssetDb;

  private final AssetRepository assetRepository;

  /**
   * Creates and initializes new asset
   */
  ImageAssetImpl(GcpAssetRepository gcpAssetRepository, Storage storage,
      AssetRepository assetRepository, String bucketName, String pathPrefix,
      MimeContentFileType filetype, String inputName, String inputDescription, String gcpHost) {
    super(gcpAssetRepository, storage, bucketName, pathPrefix, filetype, gcpHost);
    this.assetRepository = assetRepository;
    imageAssetDb = ImageAssetDb.builder().name(inputName).description(inputDescription).build();
    super.connectToBlob(imageAssetDb::setBlobAsset);
  }

  /**
   * Creates new asset from data source
   */
  ImageAssetImpl(GcpAssetRepository gcpAssetRepository, Storage storage, AssetRepository assetRepository,
      ImageAssetDb imageAssetDb) {
    super(gcpAssetRepository, storage, imageAssetDb.getGcpAsset());
    this.assetRepository = assetRepository;
    this.imageAssetDb = imageAssetDb;
  }

  @Override
  public void save() {
    super.save();
    imageAssetDb = assetRepository.saveAndFlush(imageAssetDb);
  }

  @Override
  public void linkToPlace(Place place) {
//    imageAssetDb.setRefToPlaceImages();
  }

}
