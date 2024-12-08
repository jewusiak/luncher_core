package pl.luncher.v3.luncher_core.infrastructure.persistence;

import java.util.concurrent.atomic.AtomicInteger;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.contentmanagement.model.PageArrangement;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING, uses = {
    AssetDbMapper.class})
interface PageArrangementDbMapper {
  
  PageArrangement toDomain(PageArrangementDb pageArrangementDb);

  PageArrangementDb toDb(PageArrangement pageArrangement);

  @AfterMapping
  default void ensureListMappingsAreCorrect(@MappingTarget PageArrangementDb db) {
    if (db.getSections() != null) {
      AtomicInteger sectionIdx = new AtomicInteger(0);
      db.getSections().forEach(s -> {
        // in sections
        s.setPageArrangement(db);
        s.setListIndex(sectionIdx.getAndIncrement());

        if (s.getSectionElements() != null) {
          AtomicInteger elementIdx = new AtomicInteger(0);
          s.getSectionElements().forEach(se -> {
            // in section elements
            se.setSection(s);
            se.setListIndex(elementIdx.getAndIncrement());
          });
        }

      });
    }
  }
}
