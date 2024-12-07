package pl.luncher.v3.luncher_core.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetInfoPersistenceService;
import pl.luncher.v3.luncher_core.contentmanagement.domainservices.ArrangementsPersistenceService;
import pl.luncher.v3.luncher_core.contentmanagement.model.PageArrangement;

@RequiredArgsConstructor
@Service
class ArrangementsJpaPersistenceService implements ArrangementsPersistenceService {

  private final PageArrangementsRepository pageArrangementsRepository;
  private final PlaceRepository placeRepository;
  private final PlaceTypeRepository placeTypeRepository;
  private final PageArrangementDbMapper pageArrangementDbMapper;
  private final AssetRepository assetRepository;
  private final AssetInfoPersistenceService assetInfoPersistenceService;

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

    db.getSections()
        .forEach(section -> {
          section.getSectionElements().forEach(this::assignElementsSourceItem);
        });

    return pageArrangementDbMapper.toDomain(pageArrangementsRepository.save(db));
  }

  private void assignElementsSourceItem(SectionElementDb element) {
    String sourceElementId = element.getSourceElementId();
    switch (element.getElementType()) {
      case PLACE:
        // if place/placetype does not exist - throw error, if null source element - ignore
        Optional.ofNullable(sourceElementId).map(UUID::fromString).map(placeRepository::findById)
            .ifPresent(opt -> opt.ifPresent(element::setPlace));
        element.setPlace(
            placeRepository.findById(UUID.fromString()).orElseThrow());
        break;
      case PLACE_TYPE:
        element.setPlaceType(
            placeTypeRepository.findById(element.getSourceElementId()).orElseThrow());
        break;
      //skip other types
    }
  }

  @Override
  public void deleteById(UUID id) {
    pageArrangementsRepository.deleteById(id);
  }
}
