package pl.luncher.v3.luncher_core.common.persistence.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.luncher.v3.luncher_core.common.persistence.models.UserDb;


public interface UserRepository extends JpaRepository<UserDb, String> {

  Optional<UserDb> findUserByEmail(String email);

  Optional<UserDb> findUserByUuid(UUID uuid);
}