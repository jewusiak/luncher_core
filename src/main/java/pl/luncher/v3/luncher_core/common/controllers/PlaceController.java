package pl.luncher.v3.luncher_core.common.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
import pl.luncher.v3.luncher_core.common.domain.place.PlaceFactory;
import pl.luncher.v3.luncher_core.common.domain.place.domain.Place;
import pl.luncher.v3.luncher_core.common.domain.placesearch.PlaceSearchFactory;
import pl.luncher.v3.luncher_core.common.domain.users.User;
import pl.luncher.v3.luncher_core.common.domain.users.UserFactory;
import pl.luncher.v3.luncher_core.common.model.requests.PlaceCreateRequest;
import pl.luncher.v3.luncher_core.common.model.requests.PlaceOwnerUpdateRequest;
import pl.luncher.v3.luncher_core.common.model.requests.PlaceSearchRequest;
import pl.luncher.v3.luncher_core.common.model.requests.PlaceUpdateRequest;
import pl.luncher.v3.luncher_core.common.model.responses.BasicPlaceResponse;
import pl.luncher.v3.luncher_core.common.model.responses.PlaceSearchResponse;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole.hasRole;

@Tag(name = "place", description = "Places CRUD")
@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {

  private final PlaceFactory placeFactory;
  private final UserFactory userFactory;
  private final PlaceSearchFactory placeSearchFactory;

  @GetMapping("/{uuid}")
  public ResponseEntity<?> getById(@PathVariable UUID uuid) {
    Place place = placeFactory.pullFromRepo(uuid);
    return ResponseEntity.ok(place.castToFullPlaceResponse());
  }

  @PreAuthorize(hasRole.REST_MANAGER)
  @PostMapping
  public ResponseEntity<?> createPlace(@RequestBody PlaceCreateRequest request,
      User requestingUser) {
    Place place = placeFactory.of(request, requestingUser);
    place.save();

    return ResponseEntity.ok(place.castToFullPlaceResponse());
  }

  @PreAuthorize(hasRole.REST_MANAGER)
  @PutMapping("/{placeUuid}")
  public ResponseEntity<?> updatePlace(@PathVariable UUID placeUuid,
      @RequestBody PlaceUpdateRequest placeUpdateRequest,
      User requestingUser) {
    Place place = placeFactory.pullFromRepo(placeUuid);
    place.permissions().byUser(requestingUser).edit().throwIfNotPermitted();

    place.updateWith(placeUpdateRequest);
    place.save();

    return ResponseEntity.ok(place.castToFullPlaceResponse());
  }

  @PreAuthorize(hasRole.REST_MANAGER)
  @DeleteMapping("/{placeUuid}")
  public ResponseEntity<?> removePlace(@PathVariable UUID placeUuid, User requestingUser) {

    Place place = placeFactory.pullFromRepo(placeUuid);
    place.permissions().byUser(requestingUser).delete().throwIfNotPermitted();

    return ResponseEntity.noContent().build();
  }

  @PreAuthorize(hasRole.REST_MANAGER)
  @PutMapping("/{placeUuid}/owner")
  public ResponseEntity<?> updateOwner(@PathVariable UUID placeUuid,
      @RequestBody @Valid PlaceOwnerUpdateRequest placeOwnerUpdateRequest, User requestingUser) {
    Place place = placeFactory.pullFromRepo(placeUuid);

    place.permissions().byUser(requestingUser).changeOwner().throwIfNotPermitted();

    User newOwner = userFactory.pullFromRepo(placeOwnerUpdateRequest.getEmail());

    place.changeOwner(newOwner);
    place.save();

    return ResponseEntity.ok(place.castToFullPlaceResponse());
  }

  @GetMapping
  public ResponseEntity<?> getAllPlacesPaged(@RequestParam int size, @RequestParam int page) {
    List<BasicPlaceResponse> placesList = placeFactory.pullFromRepo(size, page).stream()
        .map(Place::castToBasicPlaceResponse)
        .toList();

    return ResponseEntity.ok(placesList);
  }

  @PostMapping("/search")
  public ResponseEntity<PlaceSearchResponse> searchQuery(
      @RequestBody @Valid PlaceSearchRequest request, User user) {
    var searchRequest = placeSearchFactory.of(request);

    var list = searchRequest.fetch(request.getPage(), request.getSize());

    return ResponseEntity.ok(new PlaceSearchResponse(list));
  }
}
