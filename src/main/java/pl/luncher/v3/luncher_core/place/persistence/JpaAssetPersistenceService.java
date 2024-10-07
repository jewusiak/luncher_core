package pl.luncher.v3.luncher_core.place.persistence;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetPersistenceService;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.place.persistence.model.PlaceDb;
import pl.luncher.v3.luncher_core.place.persistence.model.mappers.AssetDbMapper;
import pl.luncher.v3.luncher_core.place.persistence.repositories.AssetRepository;
import pl.luncher.v3.luncher_core.place.persistence.repositories.PlaceRepository;

@RequiredArgsConstructor
@Service
class JpaAssetPersistenceService implements AssetPersistenceService {

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
    return assetDbMapper.toDomain(assetRepository.save(toBeSaved));
  }

  @Override
  public Asset getById(UUID id) {
    return assetDbMapper.toDomain(assetRepository.findById(id).orElseThrow());
  }

  public void delete(Asset asset) {
    var assetDb = assetRepository.findById(asset.getId()).orElseThrow();
    assetDb.getPlace().getImages().remove(assetDb);
    assetRepository.save(assetDb);
  }
}
