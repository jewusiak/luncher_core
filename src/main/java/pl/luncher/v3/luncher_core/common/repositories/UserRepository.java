package pl.luncher.v3.luncher_core.common.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.luncher.v3.luncher_core.common.domain.infra.User;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByUuid(UUID uuid);

    boolean existsByEmail(String email);

    Page<User> findUsersByOrderBySequence(Pageable pageable);
}
