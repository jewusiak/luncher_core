package pl.luncher.v3.luncher_core.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.controllers.dtos.menus.dtos.MenuOfferDto;
import pl.luncher.v3.luncher_core.controllers.dtos.menus.mappers.MenuOfferDtoMapper;
import pl.luncher.v3.luncher_core.controllers.dtos.menus.requests.MenuOfferRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.menus.responses.MenuOfferFullResponse;
import pl.luncher.v3.luncher_core.controllers.dtos.place.mappers.PlaceDtoMapper;
import pl.luncher.v3.luncher_core.controllers.dtos.place.responses.PlaceFullResponse;
import pl.luncher.v3.luncher_core.place.domainservices.PlacePersistenceService;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.place.model.menus.MenuOffer;
import pl.luncher.v3.luncher_core.user.model.AppRole.hasRole;
import pl.luncher.v3.luncher_core.user.model.User;

@Tag(name = "place-menu-offers", description = "Menu offers management")
@RestController
@RequestMapping("/place/{placeUuid}/menuoffer")
@RequiredArgsConstructor
@PreAuthorize(hasRole.REST_MANAGER)
public class PlaceMenuOffersController {

  private final PlacePersistenceService placePersistenceService;
  private final PlaceDtoMapper placeDtoMapper;
  private final MenuOfferDtoMapper menuOfferDtoMapper;

  @GetMapping("/{uuid}")
  public ResponseEntity<MenuOfferFullResponse> getById(@PathVariable UUID placeUuid,
      @PathVariable UUID uuid, @Parameter(hidden = true) User requestingUser) {
    Place place = placePersistenceService.getById(placeUuid);
    place.permissions().byUser(requestingUser).edit().throwIfNotPermitted();

    MenuOffer menuOffer = Optional.ofNullable(place.getMenuOffers()).stream().flatMap(List::stream)
        .filter(offer -> offer.getId().equals(uuid)).findFirst().orElseThrow();

    return ResponseEntity.ok(menuOfferDtoMapper.toMenuOfferFullResponse(menuOffer));
  }

  @GetMapping
  public ResponseEntity<List<MenuOfferDto>> getAll(@PathVariable UUID placeUuid,
      @Parameter(hidden = true) User requestingUser) {
    Place place = placePersistenceService.getById(placeUuid);
    place.permissions().byUser(requestingUser).edit().throwIfNotPermitted();

    List<MenuOffer> menuOffers = Optional.ofNullable(place.getMenuOffers()).orElse(List.of());

    return ResponseEntity.ok(menuOffers.stream().map(menuOfferDtoMapper::toDto).collect(Collectors.toList()));
  }

  @PostMapping
  public ResponseEntity<PlaceFullResponse> addNewOffer(@PathVariable UUID placeUuid,
      @RequestBody @Valid MenuOfferRequest request, @Parameter(hidden = true) User requestingUser) {
    Place place = placePersistenceService.getById(placeUuid);
    place.permissions().byUser(requestingUser).edit().throwIfNotPermitted();

    MenuOffer toBeCreated = menuOfferDtoMapper.creationToDomain(request);

    place.addMenuOffer(toBeCreated);

    Place savedPlace = placePersistenceService.save(place);

    return ResponseEntity.ok(placeDtoMapper.toPlaceFullResponse(savedPlace));
  }


  @PutMapping("/{menuOptionUuid}")
  public ResponseEntity<PlaceFullResponse> updateMenuOffer(@PathVariable UUID placeUuid,
      @PathVariable UUID menuOptionUuid, @RequestBody @Valid MenuOfferRequest request, @Parameter(hidden = true) User requestingUser) {
    Place place = placePersistenceService.getById(placeUuid);
    place.permissions().byUser(requestingUser).edit().throwIfNotPermitted();

    // this should keep the reference the ssame as in place object (so it will be edited by the below mapper)
    MenuOffer menuOffer = Optional.ofNullable(place.getMenuOffers()).stream().flatMap(List::stream)
        .filter(offer -> offer.getId().equals(menuOptionUuid)).findFirst().orElseThrow();

    menuOfferDtoMapper.updateDomain(menuOffer, request);

    Place savedPlace = placePersistenceService.save(place);

    return ResponseEntity.ok(placeDtoMapper.toPlaceFullResponse(savedPlace));
  }

  @DeleteMapping("/{menuOptionUuid}")
  public ResponseEntity<PlaceFullResponse> deleteMenuOffer(@PathVariable UUID placeUuid,
      @PathVariable UUID menuOptionUuid, @Parameter(hidden = true) User requestingUser) {
    Place place = placePersistenceService.getById(placeUuid);
    place.permissions().byUser(requestingUser).edit().throwIfNotPermitted();

    if (!place.getMenuOffers().removeIf(offer -> offer.getId().equals(menuOptionUuid))) {
      throw new EntityNotFoundException("Menu Offer to delete not found!");
    }

    Place savedPlace = placePersistenceService.save(place);

    return ResponseEntity.ok(placeDtoMapper.toPlaceFullResponse(savedPlace));

  }


}
