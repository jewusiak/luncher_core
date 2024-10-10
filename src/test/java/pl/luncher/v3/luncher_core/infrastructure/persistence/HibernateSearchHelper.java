package pl.luncher.v3.luncher_core.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HibernateSearchHelper {

  private final EntityManager entityManager;


  public void refreshIndexes() {
    Search.mapping(entityManager.getEntityManagerFactory()).scope(PlaceDb.class).workspace()
        .refresh();
  }
}
