package pl.luncher.v3.luncher_core.it.steps;

import io.cucumber.java.en.And;
import lombok.RequiredArgsConstructor;
import pl.luncher.common.infrastructure.persistence.HibernateSearchHelper;

@RequiredArgsConstructor
public class UtilSteps {

  private final HibernateSearchHelper hibernateSearchHelper;

  @And("Refresh Hibernate Search indexes")
  public void refreshIndexes() {
    hibernateSearchHelper.refreshIndexes();
  }
}
