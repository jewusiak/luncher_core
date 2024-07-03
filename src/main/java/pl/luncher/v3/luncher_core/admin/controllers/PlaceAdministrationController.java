package pl.luncher.v3.luncher_core.admin.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import pl.luncher.v3.luncher_core.admin.model.requests.AdminPlaceCreationRequest;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminUpdatePlaceRequest;
import pl.luncher.v3.luncher_core.common.model.responses.FullPlaceResponse;
import pl.luncher.v3.luncher_core.common.model.responses.BasicPlaceResponse;
import pl.luncher.v3.luncher_core.common.domain.infra.AppRole;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.place.Place;
import pl.luncher.v3.luncher_core.common.place.PlaceFactory;
import pl.luncher.v3.luncher_core.common.model.dto.OpeningWindowDto;

@Tag(name = "places-administration", description = "Places administration")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/places")
@PreAuthorize(AppRole.hasRole.SYS_ADMIN)
public class PlaceAdministrationController {

  private final PlaceFactory placeFactory;

  @Operation(summary = "Get all places")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved places", content = @Content(mediaType = "application/json", schema = @Schema(implementation = IterableAdminBasicPlaceResponse.class))),})
  @GetMapping("")
  public ResponseEntity<?> getAllPlacesPaged(@RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "0") int page) {
    return ResponseEntity.ok(
        placeFactory.pullFromRepo(size, page).stream().map(Place::castToBasicPlaceResponse)
            .toList());
  }

  @Operation(summary = "Get place by UUID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved place", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FullPlaceResponse.class))),
      @ApiResponse(responseCode = "404", description = "Place not found", content = @Content)})
  @GetMapping("/{uuid}")
  public ResponseEntity<FullPlaceResponse> getPlaceByUuid(@PathVariable UUID uuid) {
    Place place = placeFactory.pullFromRepo(uuid);

    return ResponseEntity.ok(place.castToFullPlaceResponse());
  }

  @Operation(summary = "Create place by admin")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully created place", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FullPlaceResponse.class))),})
  @PostMapping("")
  public ResponseEntity<FullPlaceResponse> adminCreatePlace(
      @RequestBody @Valid AdminPlaceCreationRequest request) {
    var place = placeFactory.of(request);

    place.save();

    return ResponseEntity.ok(place.castToFullPlaceResponse());
  }

  @Operation(summary = "Update place by admin", description = "Updates place basic details")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully updated place", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FullPlaceResponse.class))),})
  @PutMapping("{placeId}")
  public ResponseEntity<?> adminUpdatePlace(@RequestBody @Valid AdminUpdatePlaceRequest request,
      @PathVariable UUID placeId, User user) {
    var place = placeFactory.pullFromRepo(placeId);

// todo: permissionsFactory.of(user).canUpdate(place);   placesService.checkIfUserCanUpdatePlace(placeEntity, user);

    place.updateWith(request);
    place.save();

    return ResponseEntity.ok(place.castToFullPlaceResponse());
  }

// todo: implement  
//  @Operation(summary = "Add user to allowed users list", description = "User will be able to manage place")
//  @ApiResponses(value = {
//      @ApiResponse(responseCode = "200", description = "Successfully added user to allowed users list", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminFullPlaceResponse.class))),})
//  @PostMapping("{placeId}/allowed-users/{userId}")
//  public ResponseEntity<?> adminAddAllowedUser(@PathVariable UUID placeId,
//      @PathVariable UUID userId, User user) {
//
//    //placesService.checkIfUserCanManageAllowedUsers(placeId, user);
//    
//    Place changedPlace = placesService.addUserToAllowedUsersList(placeId, userId);
//    return ResponseEntity.ok(adminPlaceMapper.mapToFull(changedPlace));
//  }
//
//  @Operation(summary = "Remove user from allowed users list", description = "User will not be able to manage place")
//  @ApiResponses(value = {
//      @ApiResponse(responseCode = "200", description = "Successfully removed user from allowed users list", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminFullPlaceResponse.class))),})
//  @DeleteMapping("{placeId}/allowed-users/{userId}")
//  public ResponseEntity<?> adminRemoveAllowedUser(@PathVariable UUID placeId,
//      @PathVariable UUID userId, User user) {
//    placesService.checkIfUserCanManageAllowedUsers(placeId, user);
//    Place changedPlace = placesService.removeUserFromAllowedUsersList(placeId, userId);
//    return ResponseEntity.ok(adminPlaceMapper.mapToFull(changedPlace));
//  }

  @Operation(summary = "Add opening window to place", description = "Opening window is a time frame when place is open")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully added opening window", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FullPlaceResponse.class))),})
  @PutMapping("{placeUuid}/opening-windows")
  public ResponseEntity<FullPlaceResponse> addOpeningWindow(@PathVariable UUID placeUuid,
      @RequestBody
      OpeningWindowDto openingWindow) {
    var place = placeFactory.pullFromRepo(placeUuid);

    place.addOpeningWindow(openingWindow);

    place.save();

    return ResponseEntity.ok(place.castToFullPlaceResponse());
  }

  @Operation(summary = "Remove opening window from place", description = "Opening window is a time frame when place is open")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully removed opening window", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FullPlaceResponse.class))),})
  @DeleteMapping("{placeUuid}/opening-windows/{openingWindowId}")
  public ResponseEntity<FullPlaceResponse> removeOpeningWindow(@PathVariable UUID placeUuid,
      @PathVariable UUID openingWindowId) {
    var place = placeFactory.pullFromRepo(placeUuid);

    place.removeOpeningWindow(openingWindowId);

    place.save();

    return ResponseEntity.ok(place.castToFullPlaceResponse());
  }

  @Operation(summary = "Full text place search", description = "Empty query will return all places")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved places", content = @Content(mediaType = "application/json", schema = @Schema(implementation = IterableAdminBasicPlaceResponse.class))),})
  @GetMapping("/search")
  public ResponseEntity<?> adminSearchPlaces(
      @RequestParam(required = false) String query,
      @RequestParam(defaultValue = "20", required = false) int size,
      @RequestParam(defaultValue = "0", required = false) int page) {

    var places = placeFactory.pullFromRepo(query, size, page);

    return ResponseEntity.ok(places.stream().map(Place::castToBasicPlaceResponse).toList());
  }

  // swagger schema class
  interface IterableAdminBasicPlaceResponse extends Iterable<BasicPlaceResponse> {

  }
}
