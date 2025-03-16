package pl.luncher.common.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

class CommonSearchIndexer {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  @EventListener(ApplicationReadyEvent.class)
  public void onAppLoad() throws InterruptedException {
    SearchSession searchSession = Search.session(entityManager);

    MassIndexer indexer = searchSession.massIndexer(UserDb.class, PlaceDb.class)
        .threadsToLoadObjects(4);

    indexer.startAndWait();
  }
}
