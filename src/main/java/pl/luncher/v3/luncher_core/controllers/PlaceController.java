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
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetFactory;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetFilePersistenceService;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetInfoPersistenceService;
import pl.luncher.v3.luncher_core.assets.domainservices.exceptions.CannotEstablishFileTypeException;
import pl.luncher.v3.luncher_core.assets.model.AssetUploadStatus;
import pl.luncher.v3.luncher_core.assets.model.MimeContentFileType;
import pl.luncher.v3.luncher_core.configuration.security.PermitAll;
import pl.luncher.v3.luncher_core.controllers.dtos.assets.mappers.AssetDtoMapper;
import pl.luncher.v3.luncher_core.controllers.dtos.common.AssetBasicResponse;
import pl.luncher.v3.luncher_core.controllers.dtos.place.mappers.PlaceDtoMapper;
import pl.luncher.v3.luncher_core.controllers.dtos.place.requests.PlaceCreateRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.place.requests.PlaceSearchRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.place.requests.PlaceUpdateRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.place.responses.PlaceBasicResponse;
import pl.luncher.v3.luncher_core.controllers.dtos.place.responses.PlaceFullResponse;
import pl.luncher.v3.luncher_core.place.domainservices.PlacePersistenceService;
import pl.luncher.v3.luncher_core.place.domainservices.PlaceSearchService;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.user.domainservices.UserPersistenceService;
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
  private final UserPersistenceService userPersistenceService;
  private final AssetInfoPersistenceService assetInfoPersistenceService;
  private final AssetFilePersistenceService assetFilePersistenceService;
  private final AssetDtoMapper assetDtoMapper;

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

    User newOwner = null;

    if (placeUpdateRequest.getOwnerEmail() != null && !placeUpdateRequest.getOwnerEmail()
        .equalsIgnoreCase(place.getOwner().getEmail())) {
      place.permissions().byUser(requestingUser).changeOwner()
          .throwIfNotPermitted();
      newOwner = userPersistenceService.getByEmail(placeUpdateRequest.getOwnerEmail());
    }

    place = placeDtoMapper.updateDomain(placeUpdateRequest, newOwner, place);
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

    place.getImages().forEach(asset -> {
      assetFilePersistenceService.delete(asset);
      assetInfoPersistenceService.delete(asset);
    });

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

  @PostMapping(value = "/{placeUuid}/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @Operation(summary = "Upload image and link to place")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Image created and linked to place", content = @Content(schema = @Schema(implementation = AssetBasicResponse.class))),
      @ApiResponse(responseCode = "403", description = "User has no permission to edit place"),
      @ApiResponse(responseCode = "404", description = "Place not found")
  })
  @PreAuthorize(hasRole.REST_MANAGER)
  public ResponseEntity<AssetBasicResponse> addImage(@PathVariable UUID placeUuid,
      @RequestParam(required = false) String description,
      @RequestPart(value = "file") MultipartFile file,
      @Parameter(hidden = true) User requestingUser) throws IOException {

    MimeContentFileType fileType = MimeContentFileType.fromFilename(file.getOriginalFilename());

    if (fileType == null) {
      fileType = MimeContentFileType.byMimeType(file.getContentType());
    }

    if (fileType == null) {
      throw new CannotEstablishFileTypeException();
    }

    var place = placePersistenceService.getById(placeUuid);

    place.permissions().byUser(requestingUser).edit().throwIfNotPermitted();

    var asset = AssetFactory.newFilesystemPersistent(description, place);

    asset = assetInfoPersistenceService.save(asset);

    asset.setMimeType(fileType);
    asset.setOriginalFilename(file.getOriginalFilename());

    asset.validate();

    // set storage path
    assetFilePersistenceService.saveFileToStorage(asset, file.getInputStream());
    asset.setAccessUrl("/asset/" + asset.getId());

    asset.setUploadStatus(AssetUploadStatus.UPLOADED);

    asset = assetInfoPersistenceService.save(asset);

    return ResponseEntity.ok(assetDtoMapper.toAssetBasicResponse(asset));
  }

  @PostMapping(value = "/search", produces = "application/json; charset=UTF-8")
  @PermitAll
  public ResponseEntity<List<PlaceFullResponse>> searchQuery(
      @RequestBody @Valid PlaceSearchRequest request,
      @Parameter(hidden = true) User requestingUser) {

    UUID ownerUuid = null;

    // anonymous or default role
    if (requestingUser == null || requestingUser.getRole() == AppRole.USER) {
      request.setEnabled(true);
      request.setOwnerEmail(null);
    } else if (requestingUser.getRole().compareRoleTo(AppRole.SYS_MOD) < 0) {
      // role less than SYS_MOD can only see their own places
      ownerUuid = requestingUser.getUuid();
    }
    
    if (request.getOwnerEmail() != null && ownerUuid == null) {
      ownerUuid = userPersistenceService.getByEmail(request.getOwnerEmail()).getUuid();
    }
    

    var searchRequest = placeDtoMapper.toSearchRequest(request, ownerUuid);
    
    List<Place> searchResponse = placeSearchService.search(searchRequest);

    List<PlaceFullResponse> responseList = searchResponse.stream()
        .map(placeDtoMapper::toPlaceFullResponse).toList();

    return ResponseEntity.ok(responseList);
  }
}
