package pl.luncher.v3.luncher_core.common.assets;

import com.google.cloud.storage.Storage;
import java.util.UUID;
import pl.luncher.v3.luncher_core.common.persistence.models.CommonAssetDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.AssetRepository;
import pl.luncher.v3.luncher_core.common.persistence.repositories.GcpAssetRepository;
import pl.luncher.v3.luncher_core.common.place.Place;


class CommonAssetImpl extends GcpBlobAsset {

  private CommonAssetDb commonAssetDb;

  private final AssetRepository assetRepository;

  /**
   * Creates and initializes new asset
   */
  CommonAssetImpl(GcpAssetRepository gcpAssetRepository, Storage storage,
      AssetRepository assetRepository, String bucketName, String pathPrefix,
      MimeContentFileType filetype, String inputName, String inputDescription, String gcpHost) {
    super(gcpAssetRepository, storage, bucketName, pathPrefix, filetype, gcpHost);
    this.assetRepository = assetRepository;
    commonAssetDb = CommonAssetDb.builder().name(inputName).description(inputDescription).build();
    super.connectToBlob(commonAssetDb::setBlobAsset);
  }

  /**
   * Creates new asset from data source
   */
  CommonAssetImpl(GcpAssetRepository gcpAssetRepository, Storage storage,
      AssetRepository assetRepository,
      CommonAssetDb commonAssetDb) {
    super(gcpAssetRepository, storage, commonAssetDb.getGcpAsset());
    this.assetRepository = assetRepository;
    this.commonAssetDb = commonAssetDb;
  }

  @Override
  public void save() {
    super.save();
    commonAssetDb = assetRepository.saveAndFlush(commonAssetDb);
  }

  @Override
  public UUID getAssetId() {
    return commonAssetDb.getUuid();
  }

  @Override
  public void setPlaceRef(Place place) {
    commonAssetDb.setRefToPlaceImages(place.getAssetToPlaceConnectorWithRef().getPlaceDb());
  }
}
