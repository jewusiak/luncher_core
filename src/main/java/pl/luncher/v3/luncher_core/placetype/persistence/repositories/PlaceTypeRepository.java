package pl.luncher.v3.luncher_core.placetype.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import pl.luncher.v3.luncher_core.placetype.persistence.model.PlaceTypeDb;

public interface PlaceTypeRepository extends JpaRepository<PlaceTypeDb, String> {
  
}
