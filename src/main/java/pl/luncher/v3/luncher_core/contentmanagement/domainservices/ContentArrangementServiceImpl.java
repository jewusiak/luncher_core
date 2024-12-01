package pl.luncher.v3.luncher_core.contentmanagement.domainservices;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.contentmanagement.model.PageArrangement;

@Service
@RequiredArgsConstructor
class ContentArrangementServiceImpl implements ContentArrangementService {

  private final ArrangementsPersistenceService arrangementsPersistenceService;

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
    arrangement.setPrimary(old.isPrimary());

    arrangement.validate();
    return arrangementsPersistenceService.save(arrangement);
  }

  @Override
  public PageArrangement createNewArrangement(PageArrangement arrangement) {
    arrangement.setId(null);
    arrangement.setPrimary(false);
    return arrangementsPersistenceService.save(arrangement);
  }

  @Override
  @Transactional
  public PageArrangement makeArrangementPrimary(UUID uuid) {
    PageArrangement newPrimary = getArrangementById(uuid);
    PageArrangement oldPrimary = getPrimaryArrangement();
    oldPrimary.setPrimary(false);
    newPrimary.setPrimary(true);

    arrangementsPersistenceService.save(oldPrimary);
    newPrimary = arrangementsPersistenceService.save(newPrimary);

    return newPrimary;
  }

  @Override
  public void deleteArrangementById(UUID uuid) {
    var arrangement = arrangementsPersistenceService.getById(uuid);
    if (arrangement.isPrimary()) {
      throw new IllegalArgumentException(
          "This arrangement is set as primary! Cannot delete a primary arrangement.");
    }
    arrangementsPersistenceService.deleteById(arrangement.getId());
  }
}
