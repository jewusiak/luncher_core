package pl.luncher.common.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface PageArrangementsRepository extends JpaRepository<PageArrangementDb, UUID> {

  List<PageArrangementDb> findAllByPrimaryPageIsTrue();

  Optional<PageArrangementDb> findFirstByPrimaryPageIsTrue();
}
