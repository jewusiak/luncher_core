package pl.luncher.v3.luncher_core.placetype.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.luncher.v3.luncher_core.placetype.persistence.model.PlaceTypeDb;

public interface PlaceTypeRepository extends JpaRepository<PlaceTypeDb, String> {

}
