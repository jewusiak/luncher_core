package pl.luncher.v3.luncher_core.common.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.luncher.v3.luncher_core.common.domain.PlaceType;

public interface PlaceTypeRepository extends CrudRepository<PlaceType, String> {
}
