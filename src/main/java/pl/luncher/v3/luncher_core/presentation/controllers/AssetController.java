package pl.luncher.v3.luncher_core.presentation.controllers;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.requests.CreateAssetRequest;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.responses.CreateLinkAssetResponse;
import pl.luncher.v3.luncher_core.common.domain.assets.Asset;
import pl.luncher.v3.luncher_core.common.domain.assets.AssetFactory;
import pl.luncher.v3.luncher_core.common.domain.users.User;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole.hasRole;

@Tag(name = "profile", description = "User profiles")
@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
@PreAuthorize(hasRole.REST_MANAGER)
public class AssetController {

  private final AssetFactory assetFactory;
//  private final PlaceFactory placeFactory;

  @PostMapping
  @Operation(summary = "Create asset and link to place", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = CreateAssetRequest.class))))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Asset created and linked to place", content = @Content(schema = @Schema(implementation = CreateLinkAssetResponse.class))),
      @ApiResponse(responseCode = "403", description = "User has no permission to edit place"),
      @ApiResponse(responseCode = "404", description = "Place not found")
  })
  public ResponseEntity<?> create(@Valid @RequestBody CreateAssetRequest request, User requestingUser) {
//    var place = placeFactory.pullFromRepo(UUID.fromString(request.getPlaceId()));
//
//    place.permissions().byUser(requestingUser).edit().throwIfNotPermitted();
//
//    var asset = assetFactory.createCommonAsset(request.getName(), request.getDescription(),
//        request.getFileExtension());
//
//    asset.setPlace(place);
//    asset.save();
//
//    return ResponseEntity.ok(
//        new CreateLinkAssetResponse(asset.getAssetId(), asset.getUploadUrl(), asset.getAccessUrl(),
//            place.getPlaceId()));
    throw new UnsupportedOperationException("To be implemented!");
  }

  @DeleteMapping("/{uuid}")
  @Operation(summary = "Unlink and delete asset")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Asset deleted"),
      @ApiResponse(responseCode = "403", description = "User has no permission to delete asset"),
      @ApiResponse(responseCode = "404", description = "Asset not found")
  })
  public ResponseEntity<?> delete(@PathVariable UUID uuid, User requestingUser) {

    Asset asset = assetFactory.pullFromRepo(uuid);

    asset.permissions().byUser(requestingUser).delete().throwIfNotPermitted();

    asset.delete();

    return ResponseEntity.noContent().build();
  }

}
