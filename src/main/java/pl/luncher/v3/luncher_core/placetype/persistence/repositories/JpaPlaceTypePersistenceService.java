package pl.luncher.v3.luncher_core.placetype.persistence.repositories;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.placetype.domainservices.PlaceTypePersistenceService;
import pl.luncher.v3.luncher_core.placetype.model.PlaceType;
import pl.luncher.v3.luncher_core.placetype.persistence.model.PlaceTypeDb;
import pl.luncher.v3.luncher_core.placetype.persistence.model.PlaceTypeDbMapper;

@Service
@RequiredArgsConstructor
class JpaPlaceTypePersistenceService implements PlaceTypePersistenceService {

  private final PlaceTypeRepository placeTypeRepository;
  private final PlaceTypeDbMapper placeTypeDbMapper;

  @Override
  public PlaceType getByIdentifier(String identifier) {
    return placeTypeDbMapper.toDomain(placeTypeRepository.findById(identifier).orElseThrow());
  }

  @Override
  public List<PlaceType> getAll() {
    return placeTypeRepository.findAll().stream().map(placeTypeDbMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public PlaceType save(PlaceType placeType) {
    PlaceTypeDb mapperDbEntity = placeTypeDbMapper.toDbEntity(placeType);
    PlaceTypeDb saved = placeTypeRepository.save(mapperDbEntity);
    return placeTypeDbMapper.toDomain(saved);
  }

  @Override
  public void deleteByIdentifier(String identifier) {
    placeTypeRepository.deleteById(identifier);
  }
}
