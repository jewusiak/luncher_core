package pl.luncher.v3.luncher_core.infrastructure.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface MenuOfferRepository extends JpaRepository<MenuOfferDb, UUID> {

}
