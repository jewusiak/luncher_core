package pl.luncher.v3.luncher_core.it.steps;

import io.cucumber.java.en.And;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import pl.luncher.v3.luncher_core.place.persistence.model.PlaceDb;

@RequiredArgsConstructor
public class UtilSteps {

  private final EntityManager entityManager;

  @And("Refresh Hibernate Search indexes")
  public void refreshIndexes() throws InterruptedException {
    Search.mapping(entityManager.getEntityManagerFactory()).scope(PlaceDb.class).workspace()
        .refresh();
  }
}
