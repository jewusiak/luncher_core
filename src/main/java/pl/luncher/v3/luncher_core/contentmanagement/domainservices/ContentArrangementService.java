package pl.luncher.v3.luncher_core.contentmanagement.domainservices;

import java.util.List;
import java.util.UUID;
import pl.luncher.v3.luncher_core.contentmanagement.model.PageArrangement;

public interface ContentArrangementService {

  PageArrangement getPrimaryArrangement();

  List<PageArrangement> getAllPageArrangements();

  PageArrangement getArrangementById(UUID uuid);

  PageArrangement updateArrangement(PageArrangement arrangement);

  PageArrangement createNewArrangement(PageArrangement arrangement);

  PageArrangement makeArrangementPrimary(UUID uuid);

  void deleteArrangementById(UUID uuid);
}
