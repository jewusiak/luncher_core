package pl.luncher.v3.luncher_core.infrastructure.persistence;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.infrastructure.persistence.exceptions.DeleteReferencedEntityException;
import pl.luncher.v3.luncher_core.placetype.domainservices.PlaceTypePersistenceService;
import pl.luncher.v3.luncher_core.placetype.model.PlaceType;

@Service
@RequiredArgsConstructor
class PlaceTypeJpaPersistenceService implements PlaceTypePersistenceService {

  private final PlaceTypeRepository placeTypeRepository;
  private final PlaceTypeDbMapper placeTypeDbMapper;

  @Override
  public PlaceType getByIdentifier(String identifier) {
    return placeTypeDbMapper.toDomain(placeTypeRepository.findByIdentifierIgnoreCase(identifier).orElseThrow());
  }

  @Override
  public List<PlaceType> getAll() {
    return placeTypeRepository.findAll().stream().map(placeTypeDbMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public PlaceType save(PlaceType placeType) {
    PlaceTypeDb mapperDbEntity = placeTypeDbMapper.toDbEntity(placeType);
    mapperDbEntity.setIdentifier(mapperDbEntity.getIdentifier().toUpperCase());
    PlaceTypeDb saved = placeTypeRepository.save(mapperDbEntity);
    return placeTypeDbMapper.toDomain(saved);
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
