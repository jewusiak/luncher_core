package pl.luncher.v3.luncher_core.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.luncher.v3.luncher_core.common.domain.Place;

import java.util.UUID;

public interface PlaceRepository extends JpaRepository<Place, UUID> {
}