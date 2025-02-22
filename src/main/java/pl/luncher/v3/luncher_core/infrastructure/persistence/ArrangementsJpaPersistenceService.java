package pl.luncher.v3.luncher_core.infrastructure.persistence;

import java.util.List;
import java.util.NoSuchElementException;
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
    return pageArrangementDbMapper.toDomain(pageArrangementsRepository.findById(id).orElseThrow(
        () -> new NoSuchElementException("Page Arrangement with ID %s not found!".formatted(id))));
  }

  @Override
  public PageArrangement getPrimaryArrangement() {
    return pageArrangementDbMapper.toDomain(
        pageArrangementsRepository.findFirstByPrimaryPageIsTrue()
            .orElseThrow(() -> new NoSuchElementException("No primary page arrangement set.")));
  }

  @Override
  public PageArrangement save(PageArrangement pageArrangement) {
    PageArrangementDb db = pageArrangementDbMapper.toDb(pageArrangement, placeRepository,
        placeTypeRepository);

    return pageArrangementDbMapper.toDomain(pageArrangementsRepository.save(db));
  }

  @Override
  public void deleteById(UUID id) {
    pageArrangementsRepository.deleteById(id);
  }
}
