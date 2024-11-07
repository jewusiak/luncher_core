package pl.luncher.v3.luncher_core.assets.domainservices;

import java.io.IOException;
import java.io.InputStream;
import pl.luncher.v3.luncher_core.assets.model.Asset;

public interface AssetFilePersistenceService {

  /**
   * Saves to persistent storage
   *
   * @param asset      asset to assign persisted file to
   * @param dataStream stream of file contents
   */
  void saveFileToStorage(Asset asset, InputStream dataStream) throws IOException;

}
