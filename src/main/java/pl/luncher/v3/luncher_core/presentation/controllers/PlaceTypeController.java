package pl.luncher.v3.luncher_core.presentation.controllers;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole.hasRole;
import pl.luncher.v3.luncher_core.placetype.domainservices.PlaceTypePersistenceService;
import pl.luncher.v3.luncher_core.placetype.model.PlaceType;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.placetype.mappers.PlaceTypeDtoMapper;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.placetype.requests.PlaceTypeRequest;

@RequestMapping("/placetype")
@RestController
@RequiredArgsConstructor
public class PlaceTypeController {

  private final PlaceTypePersistenceService placeTypePersistenceService;
  private final PlaceTypeDtoMapper placeTypeDtoMapper;

  @PostMapping
  @PreAuthorize(hasRole.SYS_MOD)
  public ResponseEntity<?> createPlaceType(@RequestBody @Valid PlaceTypeRequest request) {
    PlaceType placeType = placeTypeDtoMapper.toDomain(request);
    placeType.validate();

    var saved = placeTypePersistenceService.save(placeType);

    return ResponseEntity.ok(placeTypeDtoMapper.toFullPlaceTypeResponse(saved));
  }

  @PutMapping("/{identifier}")
  @PreAuthorize(hasRole.SYS_MOD)
  public ResponseEntity<?> updatePlaceType(@PathVariable String identifier, @RequestBody @Valid PlaceTypeRequest request) {
    PlaceType placeType = placeTypePersistenceService.getByIdentifier(identifier);

    placeTypeDtoMapper.updateDomain(request, placeType);
    placeType.validate();

    var saved = placeTypePersistenceService.save(placeType);

    return ResponseEntity.ok(placeTypeDtoMapper.toFullPlaceTypeResponse(saved));
  }
  
  @DeleteMapping("/{identifier}")
  @PreAuthorize(hasRole.SYS_MOD)
  public ResponseEntity<?> deletePlaceType(@PathVariable String identifier) {
    placeTypePersistenceService.deleteByIdentifier(identifier);
    
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping
  public ResponseEntity<?> getAllPlaceTypes() {
    List<PlaceType> placeTypes = placeTypePersistenceService.getAll();

    return ResponseEntity.ok(placeTypes.stream().map(placeTypeDtoMapper::toFullPlaceTypeResponse)
        .collect(Collectors.toList()));
  }

  @GetMapping("/{identifier}")
  public ResponseEntity<?> getByIdentifier(@PathVariable String identifier) {
    PlaceType placeType = placeTypePersistenceService.getByIdentifier(identifier);

    return ResponseEntity.ok(placeTypeDtoMapper.toFullPlaceTypeResponse(placeType));
  }
}
