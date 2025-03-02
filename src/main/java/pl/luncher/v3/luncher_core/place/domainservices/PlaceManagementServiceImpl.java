package pl.luncher.v3.luncher_core.place.domainservices;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.iakovlev.timeshape.TimeZoneEngine;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetInfoPersistenceService;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetManagementService;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.common.interfaces.LocalDateTimeProvider;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.user.domainservices.interfaces.UserPersistenceService;
import pl.luncher.v3.luncher_core.user.model.AppRole;
import pl.luncher.v3.luncher_core.user.model.User;

@Slf4j
@Service
@RequiredArgsConstructor
class PlaceManagementServiceImpl implements PlaceManagementService {


  private final PlacePersistenceService placePersistenceService;
  private final AssetManagementService assetManagementService;
  private final AssetInfoPersistenceService assetInfoPersistenceService;
  private final UserPersistenceService userPersistenceService;
  private final PlaceSearchService placeSearchService;
  private final TimeZoneEngine timeZoneEngine;
  private final PlaceUpdateMapper placeUpdateMapper;
  private final LocalDateTimeProvider localDateTimeProvider;

  private void filterPlaceBasedOnUserPermissions(User requestingUser, Place place) {
    if (requestingUser == null || requestingUser.getRole().compareRoleTo(AppRole.REST_MANAGER) < 0) {
      place.setOwner(null);
      place.setTimeZone(null);
      var time = localDateTimeProvider.now(place.getTimeZone());
      place.getMenuOffers().removeIf(m -> {
        LocalDateTime soonestServingTime = m.getSoonestServingTime(time);
        return soonestServingTime == null || soonestServingTime.isBefore(time);
      });
    }
  }

  @Override
  public Place getPlace(UUID uuid, User requestingUser) {
    Place place = placePersistenceService.getById(uuid);

    filterPlaceBasedOnUserPermissions(requestingUser, place);
    return place;
  }

  @Override
  public List<Place> searchPlaces(PlaceSearchCommand request) {
    User owner = null;

    if (request.getRequestingUser() == null || request.getRequestingUser().getRole() == AppRole.USER) {
      // anonymous or default role
      request.setEnabled(true);
      request.setOwner(null);
    } else if (request.getRequestingUser().getRole().compareRoleTo(AppRole.SYS_MOD) < 0) {
      // role less than SYS_MOD can only see their own places
      owner = request.getRequestingUser();
    } else if (request.getOwnerEmail() != null) {
      // >= SYS_MOD
      owner = userPersistenceService.getByEmail(request.getOwnerEmail());
    }

    request.setOwner(owner);
    return placeSearchService.search(request).stream()
        .peek(place -> filterPlaceBasedOnUserPermissions(request.getRequestingUser(), place)).toList();
  }

  @Override
  public Place updatePlace(UUID uuid, Place changes, List<UUID> imageIds, User requestingUser) {
    Place place = placePersistenceService.getById(uuid);

    fetchOwner(changes);
    
    if (changes.getOwner() != null && !changes.getOwner().equals(requestingUser)) {
      place.permissions().byUser(requestingUser).changeOwner().throwIfNotPermitted();
    }
    place.permissions().byUser(requestingUser).edit().throwIfNotPermitted();

    placeUpdateMapper.updateDomain(place, changes);


    if (imageIds != null) {
      updateImages(imageIds, place);
    }

    calculateTimeZone(place);

    place.validate();
    return placePersistenceService.save(place);
  }

  @Override
  public List<Asset> fetchNewImages(List<UUID> imageIds, List<Asset> oldImages) {
    var oldImagesIds = oldImages.stream().map(Asset::getId).collect(Collectors.toSet());
    return imageIds.stream().filter(id -> !oldImagesIds.contains(id)).map(assetInfoPersistenceService::getById)
        .toList();
  }

  @Override
  public Place createPlace(Place place) {
    calculateTimeZone(place);

    place.validate();
    return placePersistenceService.save(place);
  }

  private void calculateTimeZone(Place place) {
    ZoneId timeZone = place.getTimeZone();
    if (timeZone == null) {
      if (place.getLocation() == null || place.getLocation().getLongitude() == 0
          || place.getLocation().getLatitude() == 0) {
        timeZone = ZoneId.of("UTC");
      } else {
        timeZone = timeZoneEngine.query(place.getLocation().getLatitude(),
            place.getLocation().getLongitude()).orElse(ZoneId.of("UTC"));
      }
    }
    place.setTimeZone(timeZone);
  }

  private void updateImages(List<UUID> imageIds, Place place) {
    var distinctRequestedIds = new HashSet<>(imageIds);
    if (distinctRequestedIds.size() != imageIds.size()) {
      throw new IllegalArgumentException("Image ids from request cannot have duplicates!");
    }

//      var placeImagesIds = place.getImages().stream().map(Asset::getId).collect(Collectors.toSet());
//      if (!placeImagesIds.containsAll(distinctRequestedIds)) {
//        throw new IllegalArgumentException(
//            "Requested image set has to be a subset of Place's images!");
//      }

    List<Asset> oldImages = place.getImages() == null ? new ArrayList<>() : place.getImages();

    List<Asset> newImagesList = fetchNewImages(imageIds, oldImages);
    // add old images which should be retained
    var oldImagesToRetain = oldImages.stream().filter(asset -> imageIds.contains(asset.getId()))
        .toList();

    List<Asset> imagesToSave = new ArrayList<>();
    imagesToSave.addAll(newImagesList);
    imagesToSave.addAll(oldImagesToRetain);

    imagesToSave = imagesToSave.stream()
        .sorted(Comparator.comparingInt(item -> imageIds.indexOf(item.getId()))).toList();

    place.setImages(imagesToSave);

    oldImages.stream().filter(asset -> !imageIds.contains(asset.getId()))
        .forEach(assetManagementService::deleteAsset);
  }

  private void fetchOwner(Place changes) {
    if (changes.getOwner() != null && changes.getOwner().getEmail() != null) {
      changes.setOwner(userPersistenceService.getByEmail(changes.getOwner().getEmail()));
    } else {
      changes.setOwner(null);
    }
  }
}
