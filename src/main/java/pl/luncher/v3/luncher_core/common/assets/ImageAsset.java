package pl.luncher.v3.luncher_core.common.assets;

import java.net.URI;
import lombok.AllArgsConstructor;
import pl.luncher.v3.luncher_core.common.persistence.models.ImageAssetDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.ImageAssetRepository;

@AllArgsConstructor
public class ImageAsset implements Asset {

  private ImageAssetDb imageAssetDb;
  //  private
  private final ImageAssetRepository imageAssetRepository;

  @Override
  public void save() {
    imageAssetDb = imageAssetRepository.save(imageAssetDb);
  }

  @Override
  public URI getUri() {
    return null;
  }
}
