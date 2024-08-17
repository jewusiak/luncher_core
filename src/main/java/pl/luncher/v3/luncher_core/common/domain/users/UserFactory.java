package pl.luncher.v3.luncher_core.common.domain.users;

import jakarta.persistence.EntityManager;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.common.model.requests.UserCreateRequest;
import pl.luncher.v3.luncher_core.common.model.requests.UserRegistrationRequest;
import pl.luncher.v3.luncher_core.common.persistence.models.UserDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class UserFactory {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final EntityManager entityManager;
  private final PasswordEncoder passwordEncoder;

  public User pullFromRepo(UUID uuid) {
    var userDb = userRepository.findUserByUuid(uuid).orElseThrow();

    return of(userDb);
  }

  public User pullFromRepo(String email) {
    var userDb = userRepository.findUserByEmail(email).orElseThrow();

    return of(userDb);
  }

  public User of(UserCreateRequest request) {
    var newUserDb = userMapper.map(request);

    return of(newUserDb);
  }

  public User of(UserDb userDb) {
    return new UserImpl(userDb, userRepository, userMapper, passwordEncoder);
  }

  public Page<User> findByStringQueryPaged(String query, PageRequest pageRequest) {
    if (query == null || query.isBlank()) {
      return userRepository.findAll(pageRequest).map(this::of);
    }

    SearchSession session = Search.session(entityManager);

    SearchResult<UserDb> result = session.search(UserDb.class)
        .where(f -> f.match().fields("firstname", "surname", "email").matching(query).fuzzy(2))
        .fetch(pageRequest.getPageSize() * pageRequest.getPageNumber(), pageRequest.getPageSize());

    return new PageImpl<>(result.hits(), pageRequest, result.total().hitCount()).map(this::of);
  }
  
  public User of(UserRegistrationRequest request) {
    var newUserDb = userMapper.map(request);

    return of(newUserDb);
  }
}
