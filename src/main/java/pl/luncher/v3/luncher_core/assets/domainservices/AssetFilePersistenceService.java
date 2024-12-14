package pl.luncher.v3.luncher_core.assets.domainservices;

import java.io.IOException;
import pl.luncher.v3.luncher_core.assets.model.Asset;

public interface AssetFilePersistenceService {

  /**
   * Saves to persistent storage
   *
   * @param asset      asset to persist
   */
  void saveFileToStorage(Asset asset) throws IOException;

  /**
   * Deletes file from persistent storage
   *
   * @param asset asset to delete file for
   */
  void delete(Asset asset);
}
