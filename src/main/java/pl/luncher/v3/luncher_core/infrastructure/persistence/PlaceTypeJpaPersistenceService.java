package pl.luncher.v3.luncher_core.infrastructure.persistence;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.infrastructure.persistence.exceptions.DeleteReferencedEntityException;
import pl.luncher.v3.luncher_core.infrastructure.persistence.exceptions.DuplicateEntityException;
import pl.luncher.v3.luncher_core.placetype.domainservices.PlaceTypePersistenceService;
import pl.luncher.v3.luncher_core.placetype.model.PlaceType;

@Service
@RequiredArgsConstructor
class PlaceTypeJpaPersistenceService implements PlaceTypePersistenceService {

  private final PlaceTypeRepository placeTypeRepository;
  private final PlaceTypeDbMapper placeTypeDbMapper;

  @Override
  public PlaceType getByIdentifier(String identifier) {
    return placeTypeDbMapper.toDomain(placeTypeRepository.findByIdentifierIgnoreCase(identifier)
        .orElseThrow(() -> new NoSuchElementException(
            "Place type with identifier %s not found!".formatted(identifier))));
  }

  @Override
  public List<PlaceType> getAll() {
    return placeTypeRepository.findAll().stream().map(placeTypeDbMapper::toDomain)
        .collect(Collectors.toList());
  }

  private PlaceType save(PlaceType placeType) {
    PlaceTypeDb mappedDbEntity = placeTypeDbMapper.toDbEntity(placeType);
    mappedDbEntity.setIdentifier(mappedDbEntity.getIdentifier().toUpperCase());
    PlaceTypeDb saved = placeTypeRepository.save(mappedDbEntity);
    return placeTypeDbMapper.toDomain(saved);
  }

  @Override
  public PlaceType update(PlaceType placeType) {
    if (!placeTypeRepository.existsById(placeType.getIdentifier().toUpperCase())) {
      throw new EntityNotFoundException("PlaceType with identifier " + placeType.getIdentifier() + " not found.");
    }
    return save(placeType);
  }

  @Override
  public PlaceType create(PlaceType placeType) {
    if (placeTypeRepository.existsById(placeType.getIdentifier().toUpperCase())) {
      throw new DuplicateEntityException("PlaceType with identifier " + placeType.getIdentifier() + " already exists.");
    }
    return save(placeType);
  }

  @Transactional
  @Override
  public void deleteByIdentifier(String identifier) {
    if(placeTypeRepository.existsByIdentifierAndPlacesNotEmpty(identifier)){
      throw new DeleteReferencedEntityException("PlaceType with identifier " + identifier + " has places assigned. Cannot delete.");
    }
    placeTypeRepository.deleteByIdentifierIgnoreCase(identifier);
  }
}
