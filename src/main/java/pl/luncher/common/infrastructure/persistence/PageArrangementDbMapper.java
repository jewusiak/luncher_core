package pl.luncher.common.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.contentmanagement.model.PageArrangement;
import pl.luncher.v3.luncher_core.contentmanagement.model.SectionElement;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING, uses = {
    AssetDbMapper.class, PlaceDbMapper.class, WeekDayTimeRangeMapper.class, PlaceTypeDbMapper.class,
    MenuOfferDbMapper.class, PlaceTypeDbMapper.class})
interface PageArrangementDbMapper {
  
  PageArrangement toDomain(PageArrangementDb pageArrangementDb);

  PageArrangementDb toDb(PageArrangement pageArrangement, @Context PlaceRepository placeRepository,
      @Context PlaceTypeRepository placeTypeRepository);

  SectionElementDb toDb(SectionElement sectionElement, @Context PlaceRepository placeRepository,
      @Context PlaceTypeRepository placeTypeRepository);

  @AfterMapping
  default void assignElementsSourceItem(@MappingTarget SectionElementDb targetElement,
      SectionElement sectionElement, @Context PlaceRepository placeRepository,
      @Context PlaceTypeRepository placeTypeRepository) {
    // if place/placetype does not exist - throw error, if null source element - ignore
    switch (sectionElement.getElementType()) {
      case PLACE:
        Optional.ofNullable(sectionElement.getSourceElementId()).map(UUID::fromString)
            .map(placeRepository::findById)
            .map(Optional::orElseThrow).ifPresent(targetElement::setPlace);
        break;
      case PLACE_TYPE:
        Optional.ofNullable(sectionElement.getSourceElementId()).map(placeTypeRepository::findById)
            .map(Optional::orElseThrow).ifPresent(targetElement::setPlaceType);
        break;
      //skip other types
    }
  }

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
