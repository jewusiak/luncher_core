package pl.luncher.v3.luncher_core.user.persistence.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.luncher.v3.luncher_core.user.persistence.model.ForgottenPasswordIntentDb;

public interface ForgottenPasswordIntentRepository extends JpaRepository<ForgottenPasswordIntentDb, UUID> {

}
