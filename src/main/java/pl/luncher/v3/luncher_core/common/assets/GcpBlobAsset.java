package pl.luncher.v3.luncher_core.common.assets;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobGetOption;
import com.google.cloud.storage.Storage.BlobTargetOption;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import pl.luncher.v3.luncher_core.common.persistence.models.GcpAssetDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.GcpAssetRepository;


abstract class GcpBlobAsset implements Asset {

  private final GcpAssetRepository gcpAssetRepository;
  final Storage storage;
  private Blob blob;
  private GcpAssetDb gcpAssetDb;

  /**
   * Creates and initializes new blob asset
   *
   * @param pathPrefix prefix of remote path eg. images/2021/01/ <- note the trailing slash
   */
  GcpBlobAsset(GcpAssetRepository gcpAssetRepository, Storage storage, String bucketName,
      String pathPrefix, MimeContentFileType filetype, String gcpHost) {
    this.gcpAssetRepository = gcpAssetRepository;
    this.storage = storage;
    UUID fileUuid = UUID.randomUUID();
    String filePath = pathPrefix + fileUuid + "." + filetype.getOutputExtension();
    gcpAssetDb = GcpAssetDb.builder().dateCreated(OffsetDateTime.now()).bucketName(bucketName)
        .uuid(fileUuid).path(filePath).publicUrl(buildPublicUrl(gcpHost, bucketName, filePath)).build();
    initializeRemoteFile(filetype);
  }

  /**
   * Creates new blob asset from data source
   */
  GcpBlobAsset(GcpAssetRepository gcpAssetRepository, Storage storage, GcpAssetDb gcpAssetDb) {
    this.gcpAssetRepository = gcpAssetRepository;
    this.storage = storage;
    this.gcpAssetDb = gcpAssetDb;
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
    gcpAssetDb = gcpAssetRepository.save(gcpAssetDb);
  }

  @Override
  public void delete() {
    storage.delete(getBlob().getBlobId());
//    gcpAssetRepository.deleteById(gcpAssetDb.getUuid());
  }

  @Override
  public String getAccessUrl() {
    return gcpAssetDb.getPublicUrl();
  }

  protected void connectToBlob(
      ConcreteAssetToBlobAssetConnector concreteAssetToBlobAssetConnector) {
    concreteAssetToBlobAssetConnector.connectToBlob(gcpAssetDb, GcpAssetDb.class);
  }

  private Blob getBlob() {
    if (blob != null) {
      return blob;
    }
    if (gcpAssetDb == null || gcpAssetDb.getPath() == null || gcpAssetDb.getBucketName() == null) {
      throw new IllegalStateException("blobInfoDto(.path/.bucketName) can't be null!");
    }
    blob = storage.get(gcpAssetDb.getBucketName(), gcpAssetDb.getPath());
    return blob;
  }

  private void initializeRemoteFile(MimeContentFileType fileType) {
    BlobInfo blobInfo = BlobInfo.newBuilder(gcpAssetDb.getBucketName(), gcpAssetDb.getPath())
        .setContentType(fileType.getMimeType()).build();

    blob = storage.create(blobInfo, BlobTargetOption.doesNotExist());
  }

  private String buildPublicUrl(String host, String bucketName, String objPath) {
    return "https://%s/%s/%s".formatted(host, bucketName, objPath);
  }
}
