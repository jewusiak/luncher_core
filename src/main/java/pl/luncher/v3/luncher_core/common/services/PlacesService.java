package pl.luncher.v3.luncher_core.common.services;

import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.admin.model.mappers.AdminPlaceMapper;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminUpdatePlaceRequest;
import pl.luncher.v3.luncher_core.common.domain.Place;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.repositories.PlaceRepository;

@Service
@RequiredArgsConstructor
public class PlacesService {

  private final PlaceRepository placeRepository;
  private final AdminPlaceMapper adminPlaceMapper;
  private final UserService userService;


  public Place createPlace(Place place) {
    place.setId(null);
    return savePlace(place);
  }

  public Place savePlace(Place place) {
    return placeRepository.save(place);
  }

  public Page<Place> getAllPlacesPaged(PageRequest pageRequest) {
    return placeRepository.findAll(pageRequest);
  }

  public Place getPlaceByUuid(UUID uuid) {
    return placeRepository.findById(uuid).orElseThrow(EntityNotFoundException::new);
  }

  public Page<Place> findByStringQueryPaged(String query, PageRequest pageRequest) {
    return null;
  }

  public void checkIfUserCanUpdatePlace(UUID placeId, User user) {
    checkIfUserCanUpdatePlace(getPlaceByUuid(placeId), user);
  }

  public void checkIfUserCanUpdatePlace(Place placeEntity, User user) {
    // todo: impl
  }

  public Place mapToUpdate(AdminUpdatePlaceRequest request, UUID placeId) {
    Place oldPlace = getPlaceByUuid(placeId);
    adminPlaceMapper.mapToUpdate(oldPlace, request, placeId, userService);
    return oldPlace;
  }

  public Place updatePlace(Place placeEntity, UUID placeId) {
    placeEntity.setId(placeId);
    return savePlace(placeEntity);
  }

  public Place addUserToAllowedUsersList(UUID placeId, UUID userId) {
    Place place = getPlaceByUuid(placeId);
    place.getAllowedUsers().add(userService.getUserByUuid(userId));
    return savePlace(place);
  }

  public void checkIfUserCanManageAllowedUsers(UUID placeId, User user) {
    // todo: impl
  }

  public Place removeUserFromAllowedUsersList(UUID placeId, UUID userId) {
    Place place = getPlaceByUuid(placeId);
    place.getAllowedUsers().removeIf(user -> user.getUuid().equals(userId));
    return savePlace(place);
  }
}
