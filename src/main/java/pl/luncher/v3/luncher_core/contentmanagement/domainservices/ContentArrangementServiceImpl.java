package pl.luncher.v3.luncher_core.contentmanagement.domainservices;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetInfoPersistenceService;
import pl.luncher.v3.luncher_core.contentmanagement.model.PageArrangement;

@Service
@RequiredArgsConstructor
class ContentArrangementServiceImpl implements ContentArrangementService {

  private final ArrangementsPersistenceService arrangementsPersistenceService;
  private final AssetInfoPersistenceService assetInfoPersistenceService;

  @Override
  public PageArrangement getPrimaryArrangement() {
    return arrangementsPersistenceService.getPrimaryArrangement();
  }

  @Override
  public List<PageArrangement> getAllPageArrangements() {
    return arrangementsPersistenceService.getAllArrangements();
  }

  @Override
  public PageArrangement getArrangementById(UUID uuid) {
    return arrangementsPersistenceService.getById(uuid);
  }

  @Override
  public PageArrangement updateArrangement(PageArrangement arrangement) {

    PageArrangement old = getArrangementById(arrangement.getId());
    arrangement.setPrimaryPage(old.isPrimaryPage());

    return persistPageArrangement(arrangement);
  }

  @Override
  public PageArrangement createNewArrangement(PageArrangement arrangement) {
    arrangement.setId(null);
    arrangement.setPrimaryPage(false);

    return persistPageArrangement(arrangement);
  }

  private PageArrangement persistPageArrangement(PageArrangement arrangement) {
    arrangement.getSections().forEach(section -> {
      section.getSectionElements().forEach(element -> {
        element.setThumbnail(assetInfoPersistenceService.getById(element.getThumbnail().getId()));
      });
    });

    arrangement.validate();
    return arrangementsPersistenceService.save(arrangement);
  }

  @Override
  @Transactional
  public PageArrangement makeArrangementPrimary(UUID uuid) {
    PageArrangement newPrimary = getArrangementById(uuid);
    PageArrangement oldPrimary = getPrimaryArrangement();
    oldPrimary.setPrimaryPage(false);
    newPrimary.setPrimaryPage(true);

    arrangementsPersistenceService.save(oldPrimary);
    newPrimary = arrangementsPersistenceService.save(newPrimary);

    return newPrimary;
  }

  @Override
  public void deleteArrangementById(UUID uuid) {
    var arrangement = arrangementsPersistenceService.getById(uuid);
    if (arrangement.isPrimaryPage()) {
      throw new IllegalArgumentException(
          "This arrangement is set as primary! Cannot delete a primary arrangement.");
    }
    arrangementsPersistenceService.deleteById(arrangement.getId());
  }
}
