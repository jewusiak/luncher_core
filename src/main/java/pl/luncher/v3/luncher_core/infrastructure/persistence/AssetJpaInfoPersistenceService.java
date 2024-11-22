package pl.luncher.v3.luncher_core.infrastructure.persistence;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetInfoPersistenceService;
import pl.luncher.v3.luncher_core.assets.model.Asset;

@RequiredArgsConstructor
@Service
class AssetJpaInfoPersistenceService implements AssetInfoPersistenceService {

  private final AssetRepository assetRepository;
  private final AssetDbMapper assetDbMapper;
  private final PlaceRepository placeRepository;

  @Override
  public Asset save(Asset asset) {
    PlaceDb placeDb = null;
    if (asset.getPlaceId() != null) {
      placeDb = placeRepository.findById(asset.getPlaceId()).orElseThrow();
    }
    var toBeSaved = assetDbMapper.toDbEntity(asset, placeDb);
    toBeSaved.insertPlaceListIndexValue();
    AssetDb save = assetRepository.save(toBeSaved);
    return assetDbMapper.toDomain(save);
  }

  @Override
  public Asset getById(UUID id) {
    AssetDb assetDb = assetRepository.findById(id).orElseThrow();
    
    return assetDbMapper.toDomain(assetDb);
  }

  public void delete(Asset asset) {
    var assetDb = assetRepository.findById(asset.getId()).orElseThrow();
    if (assetDb.getPlace() != null) {
      assetDb.getPlace().getImages().remove(assetDb);
    }
    assetRepository.delete(assetDb);
  }
}
