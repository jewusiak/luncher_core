package pl.luncher.v3.luncher_core.common.assets;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobTargetOption;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Getter;
import pl.luncher.v3.luncher_core.common.persistence.models.AssetDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.AssetRepository;
import pl.luncher.v3.luncher_core.common.place.Place;


@Getter(AccessLevel.PACKAGE)
class ImageAssetImpl implements Asset {

  private AssetDb assetDb;
  private final AssetRepository assetRepository;
  private final Storage storage;
  private Blob blob;

  /**
   * Creates and initializes new asset
   */
  ImageAssetImpl(String bucketName, String pathPrefix, String gcpHost, MimeContentFileType filetype,
      String fileName, String fileDescription, Storage storage, AssetRepository assetRepository) {
    this.assetRepository = assetRepository;
    this.storage = storage;

    UUID fileUuid = UUID.randomUUID();
    String filePath = pathPrefix + fileUuid + "." + filetype.getOutputExtension();
    assetDb = AssetDb.builder().dateCreated(OffsetDateTime.now()).bucketName(bucketName)
        .uuid(fileUuid).bucketPath(filePath)
        .publicUrl(buildPublicUrl(gcpHost, bucketName, filePath)).name(fileName)
        .description(fileDescription).build();
    initializeRemoteFile(filetype);
  }

  /**
   * Creates new asset from data source
   */
  ImageAssetImpl(AssetDb assetDb, Storage storage, AssetRepository assetRepository) {
    this.storage = storage;
    this.assetRepository = assetRepository;
    this.assetDb = assetDb;
  }

  @Override
  public void delete() {
    storage.delete(getBlob().getBlobId());

    // delete all in batch is used to flush the changes to the database
    assetRepository.deleteAllInBatch(Collections.singleton(assetDb));
  }

  @Override
  public String getAccessUrl() {
    return assetDb.getPublicUrl();
  }

  @Override
  public String getUploadUrl() {
    URL url = getBlob().signUrl(15, TimeUnit.MINUTES,
        Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
        Storage.SignUrlOption.withExtHeaders(Map.of("Content-Type", getBlob().getContentType())),
        Storage.SignUrlOption.withV4Signature());

    return url.toString();
  }

  @Override
  public void save() {
//    super.save();
    assetDb = assetRepository.saveAndFlush(assetDb);
  }

  @Override
  public UUID getAssetId() {
    return assetDb.getUuid();
  }

  @Override
  public void setPlace(Place place) {
    assetDb.setRefToPlaceImages(place.getDbEntity());
  }

  @Override
  public AssetPermissionsChecker permissions() {
    return new AssetPermissionsCheckerImpl(this);
  }


  private Blob getBlob() {
    if (blob != null) {
      return blob;
    }
    if (assetDb == null || assetDb.getBucketPath() == null || assetDb.getBucketName() == null) {
      throw new IllegalStateException("blobInfoDto(.path/.bucketName) can't be null!");
    }
    blob = storage.get(assetDb.getBucketName(), assetDb.getBucketPath());
    return blob;
  }

  private void initializeRemoteFile(MimeContentFileType fileType) {
    BlobInfo blobInfo = BlobInfo.newBuilder(assetDb.getBucketName(), assetDb.getBucketPath())
        .setContentType(fileType.getMimeType()).build();

    blob = storage.create(blobInfo, BlobTargetOption.doesNotExist());
  }

  private String buildPublicUrl(String host, String bucketName, String objPath) {
    return "https://%s/%s/%s".formatted(host, bucketName, objPath);
  }
}
