package pl.luncher.v3.luncher_core.infrastructure.persistence;


import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.common.exceptions.JpaExceptionsHandler;
import pl.luncher.v3.luncher_core.user.domainservices.UserPersistenceService;
import pl.luncher.v3.luncher_core.user.model.User;

@Service
@RequiredArgsConstructor
class UserJpaPersistenceService implements UserPersistenceService {

  private final UserRepository userRepository;
  private final UserDbMapper userDbMapper;

  @Override
  public List<User> getAll(int page, int size) {
    return userRepository.findAll(PageRequest.of(page, size)).map(userDbMapper::toDomain).toList();
  }

  @Override
  public User getById(UUID id) {
    return userRepository.findUserByUuid(id).map(userDbMapper::toDomain).orElseThrow();
  }

  @Override
  public User getByEmail(String email) {
    return userRepository.findUserByEmailIgnoreCase(email).map(userDbMapper::toDomain).orElseThrow();
  }

  @Override
  public User save(User user) {
    UserDb save = JpaExceptionsHandler.proxySave(userRepository::save, userDbMapper.toEntity(user));
    return userDbMapper.toDomain(save);
  }

  @Override
  public void deleteById(UUID uuid) {
    userRepository.deleteById(uuid);
  }

}

