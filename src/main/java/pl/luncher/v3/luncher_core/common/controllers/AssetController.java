package pl.luncher.v3.luncher_core.common.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.common.assets.AssetFactory;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.model.requests.CreateAssetRequest;
import pl.luncher.v3.luncher_core.common.model.responses.CreateLinkAssetResponse;
import pl.luncher.v3.luncher_core.common.permissions.PermissionCheckerFactory;
import pl.luncher.v3.luncher_core.common.place.PlaceFactory;

@RestController("/assets")
@RequiredArgsConstructor
public class AssetController {

  private final PermissionCheckerFactory permissionCheckerFactory;
  private final AssetFactory assetFactory;
  private final PlaceFactory placeFactory;

  @PostMapping
  @Operation(summary = "Create asset and link to place", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = CreateAssetRequest.class))))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Asset created and linked to place", content = @Content(schema = @Schema(implementation = CreateLinkAssetResponse.class))),
      @ApiResponse(responseCode = "403", description = "User has no permission to edit place"),
      @ApiResponse(responseCode = "404", description = "Place not found")
  })
  public ResponseEntity<?> create(@RequestBody CreateAssetRequest request, User user) {

    permissionCheckerFactory.withUser(user.getUuid())
        .withPlace(UUID.fromString(request.getPlaceId())).edit().checkPermission();

    var asset = assetFactory.createCommonAsset(request.getName(), request.getDescription(),
        request.getFileExtension());

    var place = placeFactory.pullFromRepo(UUID.fromString(request.getPlaceId()));

    asset.setPlaceRef(place);
    asset.save();

    return ResponseEntity.ok(
        new CreateLinkAssetResponse(asset.getAssetId(), asset.getUploadUrl(), asset.getAccessUrl(),
            place.getPlaceId()));
  }

  @DeleteMapping("/{uuid}")
  @Operation(summary = "Unlink and delete asset")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Asset deleted"),
      @ApiResponse(responseCode = "403", description = "User has no permission to delete asset"),
      @ApiResponse(responseCode = "404", description = "Asset not found")
  })
  public ResponseEntity<?> delete(@PathVariable UUID uuid, User user) {

    permissionCheckerFactory.withUser(user.getUuid()).withAsset(uuid).delete().checkPermission();

    assetFactory.pullFromRepo(uuid).delete();

    return ResponseEntity.noContent().build();
  }

}
