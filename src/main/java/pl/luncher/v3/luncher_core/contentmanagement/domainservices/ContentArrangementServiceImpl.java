package pl.luncher.v3.luncher_core.contentmanagement.domainservices;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetInfoPersistenceService;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.contentmanagement.domainservices.exceptions.PrimaryArrangementDeletionProhibitedException;
import pl.luncher.v3.luncher_core.contentmanagement.model.PageArrangement;
import pl.luncher.v3.luncher_core.contentmanagement.model.SectionElement;

@Component
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
    fetchThumbnails(arrangement);

    PageArrangement old = getArrangementById(arrangement.getId());
    arrangement.setPrimaryPage(old.isPrimaryPage());

    arrangement.validate();

    return arrangementsPersistenceService.save(arrangement);
  }

  @Override
  public PageArrangement createNewArrangement(PageArrangement arrangement) {
    fetchThumbnails(arrangement);

    arrangement.setId(null);
    arrangement.setPrimaryPage(false);

    arrangement.validate();

    return arrangementsPersistenceService.save(arrangement);
  }

  private void fetchThumbnails(PageArrangement arrangement) {
    if (arrangement.getSections() != null) {
      arrangement.getSections().forEach(section -> {
        if (section.getSectionElements() != null) {
          section.getSectionElements().forEach(
              element -> Optional.ofNullable(element).map(SectionElement::getThumbnail)
                  .map(Asset::getId).map(assetInfoPersistenceService::getById)
                  .ifPresent(element::setThumbnail));
        }
      });
    }
  }

  @Override
  @Transactional
  public PageArrangement makeArrangementPrimary(UUID uuid) {
    PageArrangement newPrimary = getArrangementById(uuid);
    try {
      PageArrangement oldPrimary = getPrimaryArrangement();
      oldPrimary.setPrimaryPage(false);
      arrangementsPersistenceService.save(oldPrimary);
    } catch (NoSuchElementException ignored) {

    }
    newPrimary.setPrimaryPage(true);

    newPrimary = arrangementsPersistenceService.save(newPrimary);

    return newPrimary;
  }

  @Override
  public void deleteArrangementById(UUID uuid) {
    var arrangement = arrangementsPersistenceService.getById(uuid);
    if (arrangement.isPrimaryPage()) {
      throw new PrimaryArrangementDeletionProhibitedException(
          "This arrangement is set as primary! Cannot delete a primary arrangement.");
    }
    arrangementsPersistenceService.deleteById(arrangement.getId());
  }
}
