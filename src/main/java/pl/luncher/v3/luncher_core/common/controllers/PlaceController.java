package pl.luncher.v3.luncher_core.common.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.common.domain.infra.AppRole.hasRole;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.model.requests.CreatePlaceRequest;
import pl.luncher.v3.luncher_core.common.model.responses.BasicPlaceResponse;
import pl.luncher.v3.luncher_core.common.place.Place;
import pl.luncher.v3.luncher_core.common.place.PlaceFactory;

@Tag(name = "place", description = "Places CRUD")
@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {

  private final PlaceFactory placeFactory;

  @GetMapping("/{uuid}")
  public ResponseEntity<?> getById(@PathVariable UUID uuid) {
    Place place = placeFactory.pullFromRepo(uuid);
    return ResponseEntity.ok(place.castToFullPlaceResponse());
  }

  //
//  @GetMapping
//  public ResponseEntity<?> getAllUserReadWrite(User user) {
//
//  }
//
  @PreAuthorize(hasRole.REST_USER)
  @PostMapping
  public ResponseEntity<?> createPlace(@RequestBody CreatePlaceRequest request, User user) {
    Place place = placeFactory.of(request, user);
    place.save();

    return ResponseEntity.ok(place.castToFullPlaceResponse());
  }

  @PreAuthorize(hasRole.REST_USER)
  @DeleteMapping("/{placeUuid}")
  public ResponseEntity<?> removePlace(@PathVariable UUID placeUuid, User user) {

    Place place = placeFactory.pullFromRepo(placeUuid);
    place.permissions().byUser(user).delete().throwIfnotPermitted();


    return ResponseEntity.noContent().build();
  }


  @GetMapping
  public ResponseEntity<?> getAllPlacesPaged(@RequestParam int size, @RequestParam int page) {
    List<BasicPlaceResponse> placesList = placeFactory.pullFromRepo(size, page).stream()
        .map(Place::castToBasicPlaceResponse)
        .toList();

    return ResponseEntity.ok(placesList);
  }
}
