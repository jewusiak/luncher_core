package pl.luncher.v3.luncher_core.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetFactory;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetFilePersistenceService;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetInfoPersistenceService;
import pl.luncher.v3.luncher_core.assets.domainservices.exceptions.AssetUnavailableException;
import pl.luncher.v3.luncher_core.assets.domainservices.exceptions.CannotEstablishFileTypeException;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.assets.model.AssetUploadStatus;
import pl.luncher.v3.luncher_core.assets.model.MimeContentFileType;
import pl.luncher.v3.luncher_core.configuration.properties.LuncherProperties;
import pl.luncher.v3.luncher_core.configuration.security.PermitAll;
import pl.luncher.v3.luncher_core.controllers.dtos.assets.mappers.AssetDtoMapper;
import pl.luncher.v3.luncher_core.controllers.dtos.assets.requests.CreateAssetRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.assets.responses.AssetFullResponse;
import pl.luncher.v3.luncher_core.controllers.dtos.assets.responses.CreateAssetResponse;
import pl.luncher.v3.luncher_core.place.domainservices.PlacePersistenceService;
import pl.luncher.v3.luncher_core.user.model.AppRole.hasRole;
import pl.luncher.v3.luncher_core.user.model.User;

@Tag(name = "asset", description = "Assets")
@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
@PreAuthorize(hasRole.REST_MANAGER)
public class AssetController {

  private final AssetInfoPersistenceService assetInfoPersistenceService;
  private final AssetFilePersistenceService assetFilePersistenceService;
  private final PlacePersistenceService placePersistenceService;
  private final AssetDtoMapper assetDtoMapper;
  private final LuncherProperties luncherProperties;

  @PostMapping
  @Operation(summary = "Create asset and link to place", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = CreateAssetRequest.class))))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Asset created and linked to place", content = @Content(schema = @Schema(implementation = CreateAssetResponse.class))),
      @ApiResponse(responseCode = "403", description = "User has no permission to edit place"),
      @ApiResponse(responseCode = "404", description = "Place not found")
  })
  public ResponseEntity<CreateAssetResponse> create(@Valid @RequestBody CreateAssetRequest request,
      @Parameter(hidden = true) User requestingUser) {
    var place = placePersistenceService.getById(UUID.fromString(request.getPlaceId()));

    place.permissions().byUser(requestingUser).edit().throwIfNotPermitted();

    var asset = AssetFactory.newFilesystemPersistent(request.getDescription(), place);

    asset.validate();
    var saved = assetInfoPersistenceService.save(asset);

    return ResponseEntity.ok(
        new CreateAssetResponse(saved.getId(), saved.getPlace().getId(), saved.getAccessUrl(),
            saved.getUploadStatus().name()));

  }

  @PostMapping("/{uuid}")
  @Operation(summary = "Upload asset")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Asset uploaded successfully"),
      @ApiResponse(responseCode = "403", description = "User has no permission to upload asset"),
      @ApiResponse(responseCode = "404", description = "Asset not found")
  })
  public ResponseEntity<AssetFullResponse> upload(@PathVariable UUID uuid, @RequestParam("file")
  MultipartFile file, @Parameter(hidden = true) User requestingUser) throws IOException {
    Asset asset = assetInfoPersistenceService.getById(uuid);

    asset.permissions().byUser(requestingUser.getUuid()).edit().throwIfNotPermitted();

    MimeContentFileType fileType = MimeContentFileType.fromFilename(file.getOriginalFilename());

    if (fileType == null) {
      fileType = MimeContentFileType.byMimeType(file.getContentType());
    }

    if (fileType == null) {
      throw new CannotEstablishFileTypeException();
    }

    asset.setMimeType(fileType);
    asset.setOriginalFilename(file.getOriginalFilename());

    // set storage path
    assetFilePersistenceService.saveFileToStorage(asset, file.getInputStream());
    asset.setAccessUrl("/asset/" + asset.getId());

    asset.setUploadStatus(AssetUploadStatus.UPLOADED);

    var saved = assetInfoPersistenceService.save(asset);

    return ResponseEntity.ok(assetDtoMapper.toAssetFullResponse(saved));
  }

  @DeleteMapping("/{uuid}")
  @Operation(summary = "Unlink and delete asset")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Asset deleted"),
      @ApiResponse(responseCode = "403", description = "User has no permission to delete asset"),
      @ApiResponse(responseCode = "404", description = "Asset not found")
  })
  public ResponseEntity<Void> delete(@PathVariable UUID uuid,
      @Parameter(hidden = true) User requestingUser) {

    Asset asset = assetInfoPersistenceService.getById(uuid);

    asset.permissions().byUser(requestingUser.getUuid()).delete().throwIfNotPermitted();

    assetInfoPersistenceService.delete(asset);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{uuid}/info")
  @Operation(summary = "Get Asset info by id", description = "For Admins")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Asset found"),
      @ApiResponse(responseCode = "404", description = "Asset not found")
  })
  @PreAuthorize(hasRole.SYS_MOD)
  public ResponseEntity<Resource> getById(@PathVariable UUID uuid) throws MalformedURLException {
    Asset asset = assetInfoPersistenceService.getById(uuid);

    if (asset.getUploadStatus() != AssetUploadStatus.UPLOADED) {
      throw new AssetUnavailableException("Asset is not uploaded yet");
    }

    Path path = Path.of(luncherProperties.getFilesystemPersistentAssetsBasePathWithTrailingSlash()
        + asset.getStoragePath());
    
    Resource resource = new UrlResource(path.toUri());
    if (resource.exists() || resource.isReadable()) {
      return ResponseEntity.ok().body(resource);
    }
    
    throw new AssetUnavailableException("Asset is not found on the server");
  }
  
  @GetMapping("/{uuid}")
  @Operation(summary = "Get Asset contents by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Asset found"),
      @ApiResponse(responseCode = "404", description = "Asset not found")
  })
  @PermitAll
  public ResponseEntity<AssetFullResponse> getInfoById(@PathVariable UUID uuid) {
    Asset asset = assetInfoPersistenceService.getById(uuid);

    return ResponseEntity.ok(assetDtoMapper.toAssetFullResponse(asset));
  }

}
