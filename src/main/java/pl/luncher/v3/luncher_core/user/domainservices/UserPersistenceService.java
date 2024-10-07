package pl.luncher.v3.luncher_core.user.domainservices;

import java.util.List;
import java.util.UUID;
import pl.luncher.v3.luncher_core.user.model.User;

public interface UserPersistenceService {

  List<User> getAll(int page, int size);

  User getById(UUID id);

  User getByEmail(String email);

  User save(User user);

  void deleteById(UUID uuid);
}
