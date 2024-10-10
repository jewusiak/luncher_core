package pl.luncher.v3.luncher_core.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface PlaceTypeRepository extends JpaRepository<PlaceTypeDb, String> {

}
