package pl.luncher.v3.luncher_core.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.contentmanagement.domainservices.ArrangementsPersistenceService;
import pl.luncher.v3.luncher_core.contentmanagement.model.PageArrangement;

@RequiredArgsConstructor
@Service
class ArrangementsJpaPersistenceService implements ArrangementsPersistenceService {

  private final PageArrangementsRepository pageArrangementsRepository;
  private final PlaceRepository placeRepository;
  private final PlaceTypeRepository placeTypeRepository;
  private final PageArrangementDbMapper pageArrangementDbMapper;

  @Override
  public List<PageArrangement> getAllArrangements() {
    return pageArrangementsRepository.findAll().stream().map(pageArrangementDbMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public PageArrangement getById(UUID id) {
    return pageArrangementDbMapper.toDomain(pageArrangementsRepository.findById(id).orElseThrow());
  }

  @Override
  public PageArrangement getPrimaryArrangement() {
    return pageArrangementDbMapper.toDomain(
        pageArrangementsRepository.findFirstByPrimaryPageIsTrue().orElseThrow());
  }

  @Override
  public PageArrangement save(PageArrangement pageArrangement) {
    PageArrangementDb db = pageArrangementDbMapper.toDb(pageArrangement);

    if (db.getSections() != null) {
      db.getSections()
          .forEach(section -> {
            if (section.getSectionElements() != null) {
              section.getSectionElements().forEach(this::assignElementsSourceItem);
            }
          });
    }
    return pageArrangementDbMapper.toDomain(pageArrangementsRepository.save(db));
  }

  private void assignElementsSourceItem(SectionElementDb element) {
    // if place/placetype does not exist - throw error, if null source element - ignore
    switch (element.getElementType()) {
      case PLACE:
        Optional.ofNullable(element.getSourceElementId()).map(UUID::fromString)
            .map(placeRepository::findById)
            .map(Optional::orElseThrow).ifPresent(element::setPlace);
        break;
      case PLACE_TYPE:
        Optional.ofNullable(element.getSourceElementId()).map(placeTypeRepository::findById)
            .map(Optional::orElseThrow).ifPresent(element::setPlaceType);
        break;
      //skip other types
    }
  }

  @Override
  public void deleteById(UUID id) {
    pageArrangementsRepository.deleteById(id);
  }
}
