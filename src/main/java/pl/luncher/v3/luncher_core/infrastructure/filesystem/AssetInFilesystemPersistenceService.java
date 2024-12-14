package pl.luncher.v3.luncher_core.infrastructure.filesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetFilePersistenceService;
import pl.luncher.v3.luncher_core.assets.model.Asset;

@Service
@RequiredArgsConstructor
class AssetInFilesystemPersistenceService implements AssetFilePersistenceService {

  private final FilesystemPersistentAssetsBasePathGetter filesystemPersistentAssetsBasePathGetter;

  @Override
  public void saveFileToStorage(Asset asset) throws IOException {
    String filename = "%s.%s".formatted(asset.getId(), asset.getMimeType().getOutputExtension());
    Files.write(Path.of(
            filesystemPersistentAssetsBasePathGetter.getFilesystemPersistentAssetsBasePathWithTrailingSlash()
                + filename), asset.getContent(), StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.CREATE);

    asset.setStoragePath(filename);
  }

  @Override
  public void delete(Asset asset) {
    try {
      Files.delete(Path.of(
          filesystemPersistentAssetsBasePathGetter.getFilesystemPersistentAssetsBasePathWithTrailingSlash()
              + asset.getStoragePath()));
    } catch (IOException e) {
      throw new RuntimeException("Could not delete asset file", e);
    }
  }
}
