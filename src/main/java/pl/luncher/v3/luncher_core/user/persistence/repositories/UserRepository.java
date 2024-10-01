package pl.luncher.v3.luncher_core.user.persistence.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.luncher.v3.luncher_core.user.persistence.model.UserDb;


public interface UserRepository extends JpaRepository<UserDb, String> {

  Optional<UserDb> findUserByEmail(String email);

  Optional<UserDb> findUserByUuid(UUID uuid);
}
