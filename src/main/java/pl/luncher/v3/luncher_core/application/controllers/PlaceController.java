package pl.luncher.v3.luncher_core.application.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.application.configuration.security.PermitAll;
import pl.luncher.v3.luncher_core.application.controllers.dtos.place.mappers.PlaceDtoMapper;
import pl.luncher.v3.luncher_core.application.controllers.dtos.place.requests.PlaceCreateRequest;
import pl.luncher.v3.luncher_core.application.controllers.dtos.place.requests.PlaceSearchRequest;
import pl.luncher.v3.luncher_core.application.controllers.dtos.place.requests.PlaceUpdateRequest;
import pl.luncher.v3.luncher_core.application.controllers.dtos.place.responses.PlaceBasicResponse;
import pl.luncher.v3.luncher_core.application.controllers.dtos.place.responses.PlaceFullResponse;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetManagementService;
import pl.luncher.v3.luncher_core.place.domainservices.PlaceManagementService;
import pl.luncher.v3.luncher_core.place.domainservices.PlacePersistenceService;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.user.domainservices.interfaces.UserPersistenceService;
import pl.luncher.v3.luncher_core.user.model.AppRole.hasRole;
import pl.luncher.v3.luncher_core.user.model.User;

@Tag(name = "place", description = "Places CRUD")
@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {

  private final PlaceDtoMapper placeDtoMapper;
  private final PlacePersistenceService placePersistenceService;
  private final AssetManagementService assetManagementService;
  private final PlaceManagementService placeManagementService;

  @GetMapping("/{uuid}")
  @PermitAll
  public ResponseEntity<PlaceFullResponse> getById(@PathVariable UUID uuid,
      @Parameter(hidden = true) User requestingUser) {
    Place place = placeManagementService.getPlace(uuid, requestingUser);

    PlaceFullResponse placeFullResponse = placeDtoMapper.toPlaceFullResponse(place);
    return ResponseEntity.ok(placeFullResponse);
  }

  @PreAuthorize(hasRole.REST_MANAGER)
  @PostMapping
  public ResponseEntity<PlaceFullResponse> createPlace(
      @RequestBody @Valid PlaceCreateRequest request,
      @Parameter(hidden = true) User requestingUser) {
    Place place = placeDtoMapper.toDomain(request, requestingUser);

    var savedPlace = placeManagementService.createPlace(place);

    return ResponseEntity.ok(placeDtoMapper.toPlaceFullResponse(savedPlace));
  }

  @PreAuthorize(hasRole.REST_MANAGER)
  @PutMapping("/{placeUuid}")
  public ResponseEntity<PlaceFullResponse> updatePlace(@PathVariable UUID placeUuid,
      @RequestBody PlaceUpdateRequest placeUpdateRequest,
      @Parameter(hidden = true) User requestingUser) {

    var changes = placeDtoMapper.toDomain(placeUpdateRequest);

    Place savedPlace = placeManagementService.updatePlace(placeUuid, changes,
        placeUpdateRequest.getImageIds(), requestingUser);

    return ResponseEntity.ok(placeDtoMapper.toPlaceFullResponse(savedPlace));
  }

  @PreAuthorize(hasRole.REST_MANAGER)
  @DeleteMapping("/{placeUuid}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Place deleted"),
      @ApiResponse(responseCode = "404", description = "Not found")
  })
  public ResponseEntity<Void> removePlace(@PathVariable UUID placeUuid,
      @Parameter(hidden = true) User requestingUser) {

    Place place = placePersistenceService.getById(placeUuid);

    place.permissions().byUser(requestingUser).delete()
        .throwIfNotPermitted();

    place.getImages().forEach(assetManagementService::deleteAsset);

    placePersistenceService.deleteById(place.getId());

    return ResponseEntity.noContent().build();

  }

  @PreAuthorize(hasRole.SYS_MOD)
  @GetMapping
  public ResponseEntity<List<PlaceBasicResponse>> getAllPlacesPaged(@RequestParam int size,
      @RequestParam int page) {
    List<PlaceBasicResponse> placesList = placePersistenceService.getAllPaged(page, size).stream()
        .map(placeDtoMapper::toBasicResponse).toList();

    return ResponseEntity.ok(placesList);
  }

  @PostMapping(value = "/search", produces = "application/json; charset=UTF-8")
  @PermitAll
  public ResponseEntity<List<PlaceFullResponse>> searchQuery(
      @RequestBody @Valid PlaceSearchRequest request,
      @Parameter(hidden = true) User requestingUser) {

    var searchRequest = placeDtoMapper.toSearchRequest(request, requestingUser);

    List<Place> searchResponse = placeManagementService.searchPlaces(searchRequest);

    List<PlaceFullResponse> responseList = searchResponse.stream()
        .map(placeDtoMapper::toPlaceFullResponse).toList();

    return ResponseEntity.ok(responseList);
  }
}
