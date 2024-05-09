package pl.luncher.v3.luncher_core.common.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.luncher.v3.luncher_core.common.domain.Place;

public interface PlaceRepository extends JpaRepository<Place, UUID> {

}