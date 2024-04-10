package pl.luncher.v3.luncher_core.admin.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.luncher.v3.luncher_core.admin.model.mappers.AdminPlaceMapper;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminCreatePlaceRequest;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminUpdatePlaceRequest;
import pl.luncher.v3.luncher_core.admin.model.responses.AdminBasicPlaceResponse;
import pl.luncher.v3.luncher_core.admin.model.responses.AdminFullPlaceResponse;
import pl.luncher.v3.luncher_core.common.domain.Place;
import pl.luncher.v3.luncher_core.common.domain.infra.AppRole;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.services.PlacesService;
import pl.luncher.v3.luncher_core.common.services.UserService;

import java.util.UUID;

@Tag(name = "places-administration", description = "Places administration")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/places")
@PreAuthorize(AppRole.hasRole.SYS_ADMIN)
public class PlaceAdministrationController {
    private final PlacesService placesService;
    private final AdminPlaceMapper adminPlaceMapper;
    private final UserService userService;

    @Operation(summary = "Get all places")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Successfully retrieved places",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PagedAdminBasicPlaceResponse.class))),})
    @GetMapping("")
    public ResponseEntity<?> getAllPlacesPaged(@RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(placesService.getAllPlacesPaged(PageRequest.of(page, size))
                                              .map(adminPlaceMapper::mapToBasic));
    }

    @Operation(summary = "Get place by UUID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Successfully retrieved place",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdminFullPlaceResponse.class))), @ApiResponse(responseCode = "404",
            description = "Place not found",
            content = @Content)})
    @GetMapping("/{uuid}")
    public ResponseEntity<?> getPlaceByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(adminPlaceMapper.mapToFull(placesService.getPlaceByUuid(uuid)));
    }

    @Operation(summary = "Create place by admin")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Successfully created place",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdminFullPlaceResponse.class))),})
    @PostMapping("")
    public ResponseEntity<?> adminCreatePlace(@RequestBody @Valid AdminCreatePlaceRequest request, User user) {
        var placeToCreate = adminPlaceMapper.mapToPlace(request, userService);

        var created = placesService.createPlace(placeToCreate);

        return ResponseEntity.ok(adminPlaceMapper.mapToFull(created));
    }

    @Operation(summary = "Update place by admin", description = "Updates place basic details")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Successfully updated place",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdminFullPlaceResponse.class))),})
    @PatchMapping("{placeId}")
    public ResponseEntity<?> adminUpdatePlace(@RequestBody @Valid AdminUpdatePlaceRequest request, @PathVariable UUID placeId, User user) {
        var placeEntity = placesService.mapToUpdate(request, placeId);

        placesService.checkIfUserCanUpdatePlace(placeEntity, user);

        Place updatedPlace = placesService.updatePlace(placeEntity, placeId);

        return ResponseEntity.ok(adminPlaceMapper.mapToFull(updatedPlace));
    }

    @Operation(summary = "Add user to allowed users list", description = "User will be able to manage place")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Successfully added user to allowed users list",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdminFullPlaceResponse.class))),})
    @PostMapping("{placeId}/allowed-users/{userId}")
    public ResponseEntity<?> adminAddAllowedUser(@PathVariable UUID placeId, @PathVariable UUID userId, User user) {
        placesService.checkIfUserCanManageAllowedUsers(placeId, user);
        Place changedPlace = placesService.addUserToAllowedUsersList(placeId, userId);
        return ResponseEntity.ok(adminPlaceMapper.mapToFull(changedPlace));
    }

    @Operation(summary = "Remove user from allowed users list", description = "User will not be able to manage place")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Successfully removed user from allowed users list",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdminFullPlaceResponse.class))),})
    @DeleteMapping("{placeId}/allowed-users/{userId}")
    public ResponseEntity<?> adminRemoveAllowedUser(@PathVariable UUID placeId, @PathVariable UUID userId, User user) {
        placesService.checkIfUserCanManageAllowedUsers(placeId, user);
        Place changedPlace = placesService.removeUserFromAllowedUsersList(placeId, userId);
        return ResponseEntity.ok(adminPlaceMapper.mapToFull(changedPlace));
    }

    @Operation(summary = "Full text place search", description = "Empty query will return all places")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Successfully retrieved places",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PagedAdminBasicPlaceResponse.class))),})
    @GetMapping("/search")
    public ResponseEntity<Page<AdminBasicPlaceResponse>> adminSearchPlaces(@RequestParam(required = false) String query, @RequestParam(
            defaultValue = "20",
            required = false) int size, @RequestParam(defaultValue = "0", required = false) int page) {
        return ResponseEntity.ok(placesService.findByStringQueryPaged(query, PageRequest.of(page, size))
                                              .map(adminPlaceMapper::mapToBasic));
    }

    // swagger schema class
    interface PagedAdminBasicPlaceResponse extends Page<AdminBasicPlaceResponse> {
    }
}
