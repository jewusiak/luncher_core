package pl.luncher.common.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HibernateSearchHelper {

  private final EntityManager entityManager;


  public void refreshIndexes() {
    Search.mapping(entityManager.getEntityManagerFactory()).scope(List.of(PlaceDb.class, UserDb.class)).workspace()
        .refresh();
  }
}
