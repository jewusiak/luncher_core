package pl.luncher.v3.luncher_core.contentmanagement.domainservices;

import java.util.List;
import java.util.UUID;
import pl.luncher.v3.luncher_core.contentmanagement.model.PageArrangement;

public interface ArrangementsPersistenceService {

  List<PageArrangement> getAllArrangements();

  PageArrangement getById(UUID id);

  PageArrangement getPrimaryArrangement();

  PageArrangement save(PageArrangement pageArrangement);

  void deleteById(UUID id);
}
