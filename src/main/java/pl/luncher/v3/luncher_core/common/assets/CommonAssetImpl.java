package pl.luncher.v3.luncher_core.common.assets;

import com.google.cloud.storage.Storage;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import pl.luncher.v3.luncher_core.common.persistence.models.CommonAssetDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.AssetRepository;
import pl.luncher.v3.luncher_core.common.persistence.repositories.GcpAssetRepository;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceRepository;
import pl.luncher.v3.luncher_core.common.place.Place;


@Getter(AccessLevel.PACKAGE)
class CommonAssetImpl extends GcpBlobAsset {

  private CommonAssetDb commonAssetDb;

  private final AssetRepository assetRepository;

  private final PlaceRepository placeRepository;

  /**
   * Creates and initializes new asset
   */
  CommonAssetImpl(GcpAssetRepository gcpAssetRepository, Storage storage,
      AssetRepository assetRepository, PlaceRepository placeRepository, String bucketName, String pathPrefix,
      MimeContentFileType filetype, String inputName, String inputDescription, String gcpHost) {
    super(gcpAssetRepository, storage, bucketName, pathPrefix, filetype, gcpHost);
    this.assetRepository = assetRepository;
    this.placeRepository = placeRepository;
    commonAssetDb = CommonAssetDb.builder().name(inputName).description(inputDescription).build();
    super.connectToBlob(commonAssetDb::setBlobAsset);
  }

  /**
   * Creates new asset from data source
   */
  CommonAssetImpl(GcpAssetRepository gcpAssetRepository, Storage storage,
      AssetRepository assetRepository, PlaceRepository placeRepository,
      CommonAssetDb commonAssetDb) {
    super(gcpAssetRepository, storage, commonAssetDb.getGcpAsset());
    this.assetRepository = assetRepository;
    this.commonAssetDb = commonAssetDb;
    this.placeRepository = placeRepository;
  }

  @Override
  public void delete() {
    super.delete();
    assetRepository.delete(commonAssetDb);
  }

  @Override
  public void save() {
//    super.save();
    commonAssetDb = assetRepository.saveAndFlush(commonAssetDb);
  }

  @Override
  public UUID getAssetId() {
    return commonAssetDb.getUuid();
  }

  @Override
  public void setPlace(Place place) {
    commonAssetDb.setRefToPlaceImages(placeRepository.findById(place.getPlaceId()).orElseThrow());
  }

  @Override
  public AssetPermissionsChecker permissions() {
    return new AssetPermissionsCheckerImpl(this);
  }
}
