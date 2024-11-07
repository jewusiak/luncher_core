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
  private final PlaceDbMapper placeDbMapper;
  private final PlaceRepository placeRepository;

  @Override
  public Asset save(Asset asset) {
    PlaceDb placeDb = null;
    if (asset.getPlace() != null) {
      placeDb = placeRepository.findById(asset.getPlace().getId()).orElseThrow();
    }
    var toBeSaved = assetDbMapper.toDbEntity(asset, placeDb);
    return assetDbMapper.toDomain(assetRepository.save(toBeSaved), placeDbMapper.toDomain(toBeSaved.getPlace()));
  }

  @Override
  public Asset getById(UUID id) {
    AssetDb assetDb = assetRepository.findById(id).orElseThrow();
    
    return assetDbMapper.toDomain(assetDb, placeDbMapper.toDomain(assetDb.getPlace()));
  }

  public void delete(Asset asset) {
    var assetDb = assetRepository.findById(asset.getId()).orElseThrow();
    if (assetDb.getPlace() != null) {
      assetDb.getPlace().getImages().remove(assetDb);
    }
    assetRepository.save(assetDb);
  }
}
