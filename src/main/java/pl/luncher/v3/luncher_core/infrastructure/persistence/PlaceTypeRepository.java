package pl.luncher.v3.luncher_core.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface PlaceTypeRepository extends JpaRepository<PlaceTypeDb, String> {
    Optional<PlaceTypeDb> findByIdentifierIgnoreCase(String identifier);
    void deleteByIdentifierIgnoreCase(String identifier);
    boolean existsByIdentifierAndPlacesNotEmpty(String identifier);
}
