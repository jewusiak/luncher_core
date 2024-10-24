package pl.luncher.v3.luncher_core.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


interface UserRepository extends JpaRepository<UserDb, UUID> {

  Optional<UserDb> findUserByEmailIgnoreCase(String email);

  Optional<UserDb> findUserByUuid(UUID uuid);
}
