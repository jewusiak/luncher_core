package pl.luncher.v3.luncher_core;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import pl.luncher.v3.luncher_core.place.persistence.model.PlaceDb;
import pl.luncher.v3.luncher_core.common.persistence.models.UserDb;

@SpringBootApplication(exclude = {ElasticsearchRestClientAutoConfiguration.class})
public class LuncherCoreApplication {

  @PersistenceContext
  private EntityManager entityManager;

  public static void main(String[] args) {
    SpringApplication.run(LuncherCoreApplication.class, args);
  }

  @Transactional
  @EventListener(ApplicationReadyEvent.class)
  public void onAppLoad() throws InterruptedException {
    SearchSession searchSession = Search.session(entityManager);

    MassIndexer indexer = searchSession.massIndexer(UserDb.class, PlaceDb.class)
        .threadsToLoadObjects(4);

    indexer.startAndWait();
  }

}
