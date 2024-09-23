package pl.luncher.v3.luncher_core.place.persistence.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.luncher.v3.luncher_core.place.persistence.model.AssetDb;

public interface AssetRepository extends JpaRepository<AssetDb, UUID> {

}
