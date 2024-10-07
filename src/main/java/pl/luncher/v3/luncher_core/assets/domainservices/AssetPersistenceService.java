package pl.luncher.v3.luncher_core.assets.domainservices;

import java.util.UUID;
import pl.luncher.v3.luncher_core.assets.model.Asset;

public interface AssetPersistenceService {

  Asset save(Asset asset);

  Asset getById(UUID id);

  void delete(Asset asset);
}
