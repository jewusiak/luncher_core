package pl.luncher.v3.luncher_core.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.user.domainservices.interfaces.UserPersistenceService;
import pl.luncher.v3.luncher_core.user.domainservices.interfaces.UserSearchService;
import pl.luncher.v3.luncher_core.user.model.User;

@Service
@RequiredArgsConstructor
@Slf4j
class UserHibernateSearchService implements UserSearchService {

  private final UserPersistenceService userPersistenceService;

  private final EntityManager entityManager;
  private final UserDbMapper userDbMapper;

  @Override
  public List<User> search(String query, int page, int size) {

    if (query == null || query.isBlank()) {
      return userPersistenceService.getAll(page, size);
    }

    SearchSession session = Search.session(entityManager);

    SearchResult<UserDb> result = session.search(UserDb.class)
        .where(f -> f.match().fields("firstname", "surname", "email").matching(query).fuzzy(2))
        .fetch(page * size, size);

    return result.hits().stream().map(userDbMapper::toDomain).collect(Collectors.toList());
  }


  @Override
  public void reindexDb() throws InterruptedException {
    log.debug("Starting PlaceDb reindexing");
    Search.session(entityManager).scope(UserDb.class).massIndexer().threadsToLoadObjects(4).startAndWait();
    log.debug("PlaceDb reindexing finished");
  }
}
