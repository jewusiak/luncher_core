package pl.luncher.v3.luncher_core.common.persistence.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceTypeDb;

public interface PlaceTypeRepository extends CrudRepository<PlaceTypeDb, String> {

}