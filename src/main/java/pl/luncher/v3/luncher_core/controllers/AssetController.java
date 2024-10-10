package pl.luncher.v3.luncher_core.controllers;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetFactory;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetPersistenceService;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.user.model.AppRole.hasRole;
import pl.luncher.v3.luncher_core.controllers.dtos.assets.mappers.AssetDtoMapper;
import pl.luncher.v3.luncher_core.controllers.dtos.assets.requests.CreateAssetRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.assets.responses.CreateAssetResponse;
import pl.luncher.v3.luncher_core.place.domainservices.PlacePersistenceService;
import pl.luncher.v3.luncher_core.place.model.UserDto;
import pl.luncher.v3.luncher_core.user.model.User;

@Tag(name = "asset", description = "Assets")
@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
@PreAuthorize(hasRole.REST_MANAGER)
public class AssetController {

  private final AssetPersistenceService assetPersistenceService;
  private final PlacePersistenceService placePersistenceService;
  private final AssetDtoMapper assetDtoMapper;

  @PostMapping
  @Operation(summary = "Create asset and link to place", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = CreateAssetRequest.class))))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Asset created and linked to place", content = @Content(schema = @Schema(implementation = CreateAssetResponse.class))),
      @ApiResponse(responseCode = "403", description = "User has no permission to edit place"),
      @ApiResponse(responseCode = "404", description = "Place not found")
  })
  public ResponseEntity<?> create(@Valid @RequestBody CreateAssetRequest request,
      User requestingUser) {
    var place = placePersistenceService.getById(UUID.fromString(request.getPlaceId()));

    place.permissions().byUser(mapUserToDtoWorkaround(requestingUser)).edit().throwIfNotPermitted();

    var asset = AssetFactory.newFilesystemPersistent(request.getDescription(), place.getId());

    asset.validate();
    var saved = assetPersistenceService.save(asset);

    return ResponseEntity.ok(
        new CreateAssetResponse(saved.getId(), saved.getPlaceId(), saved.getAccessUrl(),
            saved.getUploadStatus().name()));

  }

  private UserDto mapUserToDtoWorkaround(User requestingUser) {
    return new UserDto(requestingUser.getUuid(), requestingUser.getEmail(),
        requestingUser.getRole());
  }

  @DeleteMapping("/{uuid}")
  @Operation(summary = "Unlink and delete asset")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Asset deleted"),
      @ApiResponse(responseCode = "403", description = "User has no permission to delete asset"),
      @ApiResponse(responseCode = "404", description = "Asset not found")
  })
  public ResponseEntity<?> delete(@PathVariable UUID uuid, User requestingUser) {

    Asset asset = assetPersistenceService.getById(uuid);

    asset.permissions().byUser(requestingUser.getUuid()).delete().throwIfNotPermitted();

    assetPersistenceService.delete(asset);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{uuid}")
  @Operation(summary = "Get Asset by id", description = "For Admins")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Asset found"),
      @ApiResponse(responseCode = "404", description = "Asset not found")
  })
  @PreAuthorize(hasRole.SYS_MOD)
  public ResponseEntity<?> getById(@PathVariable UUID uuid) {
    Asset asset = assetPersistenceService.getById(uuid);

    return ResponseEntity.ok(assetDtoMapper.toAssetFullResponse(asset));
  }

}
