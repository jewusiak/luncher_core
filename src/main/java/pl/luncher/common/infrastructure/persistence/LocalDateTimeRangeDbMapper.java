package pl.luncher.common.infrastructure.persistence;

import org.mapstruct.Mapper;
import pl.luncher.v3.luncher_core.common.model.timing.LocalDateTimeRange;

@Mapper
interface LocalDateTimeRangeDbMapper {

  LocalDateTimeRange toDomain(LocalDateTimeRangeDb dbEntity);

  LocalDateTimeRangeDb toDb(LocalDateTimeRange domainObject);
}
