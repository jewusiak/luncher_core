package pl.luncher.v3.luncher_core.infrastructure.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetFilePersistenceService;
import pl.luncher.v3.luncher_core.assets.model.Asset;

@Service
@RequiredArgsConstructor
class AssetInFilesystemPersistenceService implements AssetFilePersistenceService {

  private final FilesystemPersistentAssetsBasePathGetter filesystemPersistentAssetsBasePathGetter;

  @Override
  public void saveFileToStorage(Asset asset, InputStream dataStream) throws IOException {
    String filename = asset.getId() + asset.getMimeType().getOutputExtension();

    Files.copy(dataStream, Path.of(filesystemPersistentAssetsBasePathGetter.getFilesystemPersistentAssetsBasePathWithTrailingSlash() + filename), StandardCopyOption.REPLACE_EXISTING);

    asset.setStoragePath(filename);
  }
}
