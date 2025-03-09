package pl.luncher.common.infrastructure.persistence;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetInfoPersistenceService;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.common.infrastructure.persistence.exceptions.DeleteReferencedEntityException;

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
      placeDb = placeRepository.findById(asset.getPlaceId()).orElseThrow(
          () -> new NoSuchElementException(
              "Can't find place with id %s".formatted(asset.getPlaceId())));
    }
    var toBeSaved = assetDbMapper.toDbEntity(asset, placeDb);
    toBeSaved.insertPlaceListIndexValue();
    AssetDb save = assetRepository.save(toBeSaved);
    Asset domain = assetDbMapper.toDomain(save);
    domain.setContent(asset.getContent());
    return domain;
  }

  @Override
  public Asset getById(UUID id) {
    AssetDb assetDb = assetRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Asset with ID %s not found!".formatted(id)));
    
    return assetDbMapper.toDomain(assetDb);
  }

  public void delete(Asset asset) {
    var assetDb = assetRepository.findById(asset.getId()).orElseThrow(
        () -> new NoSuchElementException("Asset with ID %s not found".formatted(asset.getId())));
    if (assetDb.getSectionElements() != null && !assetDb.getSectionElements().isEmpty()) {
      throw new DeleteReferencedEntityException(
          "Cannot delete this asset! Is still referenced by %s SectionElements of Arrangements %s.".formatted(
              Arrays.toString(assetDb.getSectionElements().stream().map(SectionElementDb::getId)
                  .toArray(UUID[]::new)), Arrays.toString(
                  assetDb.getSectionElements().stream().map(SectionElementDb::getSection)
                      .map(SectionDb::getPageArrangement).map(PageArrangementDb::getId)
                      .toArray(UUID[]::new))));
    }
    if (assetDb.getPlace() != null) {
      assetDb.getPlace().getImages().remove(assetDb);
    }
    assetRepository.delete(assetDb);
  }
}
