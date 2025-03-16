package pl.luncher.common.infrastructure.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface AssetRepository extends JpaRepository<AssetDb, UUID> {

}
