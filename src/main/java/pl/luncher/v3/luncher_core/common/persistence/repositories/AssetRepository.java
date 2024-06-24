package pl.luncher.v3.luncher_core.common.persistence.repositories;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import pl.luncher.v3.luncher_core.common.persistence.models.AssetDb;

public interface AssetRepository extends CrudRepository<AssetDb, UUID> {

}