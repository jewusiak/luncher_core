package pl.luncher.v3.luncher_core.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
import pl.luncher.v3.luncher_core.configuration.security.PermitAll;
import pl.luncher.v3.luncher_core.controllers.dtos.placetype.FullPlaceTypeResponse;
import pl.luncher.v3.luncher_core.controllers.dtos.placetype.mappers.PlaceTypeDtoMapper;
import pl.luncher.v3.luncher_core.controllers.dtos.placetype.requests.CreatePlaceTypeRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.placetype.requests.UpdatePlaceTypeRequest;
import pl.luncher.v3.luncher_core.placetype.domainservices.PlaceTypeManagementService;
import pl.luncher.v3.luncher_core.placetype.model.PlaceType;
import pl.luncher.v3.luncher_core.user.model.AppRole.hasRole;

@RequestMapping("/placetype")
@RestController
@RequiredArgsConstructor
public class PlaceTypeController {

  private final PlaceTypeManagementService placeTypeManagementService;
  private final PlaceTypeDtoMapper placeTypeDtoMapper;

  @PostMapping
  @PreAuthorize(hasRole.SYS_MOD)
  public ResponseEntity<FullPlaceTypeResponse> createPlaceType(
      @RequestBody @Valid CreatePlaceTypeRequest request) {
    PlaceType placeType = placeTypeDtoMapper.toDomain(request);

    var saved = placeTypeManagementService.createPlaceType(placeType);

    return ResponseEntity.ok(placeTypeDtoMapper.toFullPlaceTypeResponse(saved));
  }

  @PutMapping("/{identifier}")
  @PreAuthorize(hasRole.SYS_MOD)
  public ResponseEntity<FullPlaceTypeResponse> updatePlaceType(@PathVariable @NotBlank String identifier,
      @RequestBody @Valid UpdatePlaceTypeRequest request) {

    var domainObject = placeTypeDtoMapper.toDomain(request, identifier);

    var saved = placeTypeManagementService.updatePlaceType(domainObject);

    return ResponseEntity.ok(placeTypeDtoMapper.toFullPlaceTypeResponse(saved));
  }

  @DeleteMapping("/{identifier}")
  @PreAuthorize(hasRole.SYS_MOD)
  public ResponseEntity<Void> deletePlaceType(@PathVariable String identifier) {
    placeTypeManagementService.deleteByIdentifier(identifier);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping
  @PermitAll
  public ResponseEntity<List<FullPlaceTypeResponse>> getAllPlaceTypes() {
    List<PlaceType> placeTypes = placeTypeManagementService.getAllPlaceTypes();

    return ResponseEntity.ok(placeTypes.stream().map(placeTypeDtoMapper::toFullPlaceTypeResponse)
        .collect(Collectors.toList()));
  }

  @GetMapping("/{identifier}")
  @PermitAll
  public ResponseEntity<FullPlaceTypeResponse> getByIdentifier(@PathVariable String identifier) {
    PlaceType placeType = placeTypeManagementService.getByIdentifier(identifier);

    return ResponseEntity.ok(placeTypeDtoMapper.toFullPlaceTypeResponse(placeType));
  }
}
