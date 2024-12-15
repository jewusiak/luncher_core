package pl.luncher.v3.luncher_core.assets.domainservices;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.luncher.v3.luncher_core.assets.domainservices.exceptions.AssetUnavailableException;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.assets.model.AssetUploadStatus;
import pl.luncher.v3.luncher_core.configuration.properties.LuncherProperties;

@Service
@RequiredArgsConstructor
public class AssetManagementService {

  private final AssetFilePersistenceService assetFilePersistenceService;
  private final AssetInfoPersistenceService assetInfoPersistenceService;
  private final LuncherProperties luncherProperties;


  @Transactional
  public Asset createAsset(String description, String optionalBlurHash, MultipartFile file) throws IOException {

    Asset asset = AssetFactory.newFilesystemPersistent(description, file.getOriginalFilename(),
        file.getContentType(), optionalBlurHash, file.getBytes());

    asset = assetInfoPersistenceService.save(asset);

    // set storage path
    assetFilePersistenceService.saveFileToStorage(asset);
    asset.setAccessUrl("/asset/" + asset.getId());

    asset.setUploadStatus(AssetUploadStatus.UPLOADED);

    asset = assetInfoPersistenceService.save(asset);
    return asset;
  }

  @Transactional
  public void deleteAsset(Asset asset) {
    assetInfoPersistenceService.delete(asset);
    assetFilePersistenceService.delete(asset);
  }

  public Resource getAssetContents(UUID uuid) throws MalformedURLException {
    Asset asset = assetInfoPersistenceService.getById(uuid);

    if (asset.getUploadStatus() != AssetUploadStatus.UPLOADED) {
      throw new AssetUnavailableException("Asset is not uploaded yet");
    }

    Path path = Path.of(luncherProperties.getFilesystemPersistentAssetsBasePathWithTrailingSlash()
        + asset.getStoragePath());

    Resource resource = new UrlResource(path.toUri());
    if (resource.exists() || resource.isReadable()) {
      return resource;
    }

    throw new AssetUnavailableException("Asset is not found on the server");
  }

}
