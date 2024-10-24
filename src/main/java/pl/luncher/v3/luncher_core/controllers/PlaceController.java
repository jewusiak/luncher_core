package pl.luncher.v3.luncher_core.controllers;

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
import pl.luncher.v3.luncher_core.configuration.security.PermitAll;
import pl.luncher.v3.luncher_core.controllers.dtos.place.mappers.PlaceDtoMapper;
import pl.luncher.v3.luncher_core.controllers.dtos.place.requests.PlaceCreateRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.place.requests.PlaceSearchRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.place.requests.PlaceUpdateRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.place.responses.PlaceBasicResponse;
import pl.luncher.v3.luncher_core.controllers.dtos.place.responses.PlaceFullResponse;
import pl.luncher.v3.luncher_core.controllers.dtos.place.responses.PlaceSearchResponse;
import pl.luncher.v3.luncher_core.place.domainservices.PlacePersistenceService;
import pl.luncher.v3.luncher_core.place.domainservices.PlaceSearchService;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.user.model.AppRole;
import pl.luncher.v3.luncher_core.user.model.AppRole.hasRole;
import pl.luncher.v3.luncher_core.user.model.User;

@Tag(name = "place", description = "Places CRUD")
@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {

  private final PlaceDtoMapper placeDtoMapper;
  private final PlacePersistenceService placePersistenceService;
  private final PlaceSearchService placeSearchService;

  @GetMapping("/{uuid}")
  public ResponseEntity<PlaceFullResponse> getById(@PathVariable UUID uuid) {
    Place place = placePersistenceService.getById(uuid);

    return ResponseEntity.ok(placeDtoMapper.toPlaceFullResponse(place));
  }

  @PreAuthorize(hasRole.REST_MANAGER)
  @PostMapping
  public ResponseEntity<PlaceFullResponse> createPlace(
      @RequestBody @Valid PlaceCreateRequest request,
      @Parameter(hidden = true) User requestingUser) {
    Place place = placeDtoMapper.toDomain(request, requestingUser);

    place.validate();
    Place savedPlace = placePersistenceService.save(place);

    return ResponseEntity.ok(placeDtoMapper.toPlaceFullResponse(savedPlace));
  }

  @PreAuthorize(hasRole.REST_MANAGER)
  @PutMapping("/{placeUuid}")
  public ResponseEntity<PlaceFullResponse> updatePlace(@PathVariable UUID placeUuid,
      @RequestBody PlaceUpdateRequest placeUpdateRequest,
      @Parameter(hidden = true) User requestingUser) {

    Place place = placePersistenceService.getById(placeUuid);

    place.permissions().byUser(requestingUser).edit().throwIfNotPermitted();

    if (placeUpdateRequest.getOwner() != null) {
      place.permissions().byUser(requestingUser).changeOwner()
          .throwIfNotPermitted();
    }

    place = placeDtoMapper.updateDomain(placeUpdateRequest, place);
    place.validate();
    Place savedPlace = placePersistenceService.save(place);

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

    placePersistenceService.deleteById(place.getId());

    return ResponseEntity.noContent().build();

  }

  @PreAuthorize(hasRole.SYS_MOD)
  @GetMapping
  public ResponseEntity<List<PlaceBasicResponse>> getAllPlacesPaged(@RequestParam int size,
      @RequestParam int page) {
    List<PlaceBasicResponse> placesList = placePersistenceService.getAllPaged(size, page).stream()
        .map(placeDtoMapper::toBasicResponse).toList();

    return ResponseEntity.ok(placesList);
  }

  @PostMapping("/search")
  @PermitAll
  public ResponseEntity<PlaceSearchResponse> searchQuery(
      @RequestBody @Valid PlaceSearchRequest request,
      @Parameter(hidden = true) User requestingUser) {

    var searchRequest = placeDtoMapper.toSearchRequest(request);

    // anonymous or default role
    if (requestingUser == null || requestingUser.getRole() == AppRole.USER) {
      searchRequest.setEnabled(true);
      searchRequest.setOwner(null);
    } else if (requestingUser.getRole().compareRoleTo(AppRole.SYS_MOD) < 0) {
      // role less than SYS_MOD can only see their own places
      searchRequest.setOwner(requestingUser.getUuid());
    }

    List<Place> searchResponse = placeSearchService.search(searchRequest);

    List<PlaceFullResponse> responseList = searchResponse.stream()
        .map(placeDtoMapper::toPlaceFullResponse).toList();

    return ResponseEntity.ok(new PlaceSearchResponse(responseList));
  }
}
