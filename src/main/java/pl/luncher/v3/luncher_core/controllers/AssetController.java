package pl.luncher.v3.luncher_core.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetInfoPersistenceService;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetManagementService;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.assets.model.MimeContentFileType;
import pl.luncher.v3.luncher_core.configuration.security.PermitAll;
import pl.luncher.v3.luncher_core.controllers.dtos.assets.mappers.AssetDtoMapper;
import pl.luncher.v3.luncher_core.controllers.dtos.assets.responses.AssetFullResponse;
import pl.luncher.v3.luncher_core.controllers.dtos.common.AssetBasicResponse;
import pl.luncher.v3.luncher_core.place.domainservices.PlacePersistenceService;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.user.model.AppRole.hasRole;
import pl.luncher.v3.luncher_core.user.model.User;

@Tag(name = "asset", description = "Assets")
@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
public class AssetController {

  private final AssetInfoPersistenceService assetInfoPersistenceService;
  private final PlacePersistenceService placePersistenceService;
  private final AssetDtoMapper assetDtoMapper;
  private final AssetManagementService assetManagementService;


  @DeleteMapping("/{uuid}")
  @Operation(summary = "Unlink and delete asset")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Asset deleted"),
      @ApiResponse(responseCode = "403", description = "User has no permission to delete asset"),
      @ApiResponse(responseCode = "404", description = "Asset not found")
  })
  @PreAuthorize(hasRole.REST_MANAGER)
  public ResponseEntity<Void> delete(@PathVariable UUID uuid,
      @Parameter(hidden = true) User requestingUser) {

    Asset asset = assetInfoPersistenceService.getById(uuid);
    Place place = placePersistenceService.getById(asset.getPlaceId());
    place.permissions().byUser(requestingUser).edit().throwIfNotPermitted();

    assetManagementService.deleteAsset(asset);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{uuid}/info")
  @Operation(summary = "Get Asset info by id", description = "For Admins")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Asset found"),
      @ApiResponse(responseCode = "404", description = "Asset not found")
  })
  @PreAuthorize(hasRole.SYS_MOD)
  public ResponseEntity<AssetFullResponse> getInfoById(@PathVariable UUID uuid) {
    Asset asset = assetInfoPersistenceService.getById(uuid);

    return ResponseEntity.ok(assetDtoMapper.toAssetFullResponse(asset));
  }

  @GetMapping("/{uuid}")
  @Operation(summary = "Get Asset contents by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Asset found"),
      @ApiResponse(responseCode = "404", description = "Asset not found")
  })
  @PermitAll
  public ResponseEntity<Resource> getContentsById(@PathVariable UUID uuid)
      throws MalformedURLException {

    Resource contents = assetManagementService.getAssetContents(uuid);

    return ResponseEntity.ok()
        .contentType(MediaType.valueOf(
            MimeContentFileType.fromFilename(contents.getFilename()).getMimeType())).body(
            contents);
  }


  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @Operation(summary = "Upload image")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Image created", content = @Content(schema = @Schema(implementation = AssetBasicResponse.class))),
      @ApiResponse(responseCode = "403", description = "User has no permission"),
      @ApiResponse(responseCode = "404", description = "Entity not found")
  })
  @PreAuthorize(hasRole.REST_MANAGER)
  public ResponseEntity<AssetBasicResponse> uploadImage(
      @RequestParam(required = false) String description,
      @RequestPart(value = "file") MultipartFile file,
      @Parameter(hidden = true) User requestingUser) throws IOException {

    var asset = assetManagementService.createAsset(description, file);

    return ResponseEntity.ok(assetDtoMapper.toAssetBasicResponse(asset));
  }

}
