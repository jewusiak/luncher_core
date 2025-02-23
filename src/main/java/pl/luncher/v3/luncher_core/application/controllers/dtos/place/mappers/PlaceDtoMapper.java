package pl.luncher.v3.luncher_core.application.controllers.dtos.place.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import pl.luncher.v3.luncher_core.application.controllers.dtos.assets.mappers.AssetDtoMapper;
import pl.luncher.v3.luncher_core.application.controllers.dtos.common.mappers.WeekDayTimeRangeDtoMapper;
import pl.luncher.v3.luncher_core.application.controllers.dtos.menus.mappers.MenuOfferDtoMapper;
import pl.luncher.v3.luncher_core.application.controllers.dtos.place.requests.PlaceCreateRequest;
import pl.luncher.v3.luncher_core.application.controllers.dtos.place.requests.PlaceSearchRequest;
import pl.luncher.v3.luncher_core.application.controllers.dtos.place.requests.PlaceUpdateRequest;
import pl.luncher.v3.luncher_core.application.controllers.dtos.place.responses.PlaceBasicResponse;
import pl.luncher.v3.luncher_core.application.controllers.dtos.place.responses.PlaceFullResponse;
import pl.luncher.v3.luncher_core.place.domainservices.PlaceSearchCommand;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.user.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING, uses = {
    WeekDayTimeRangeDtoMapper.class,
    AssetDtoMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class PlaceDtoMapper {

  @Autowired
  private MenuOfferDtoMapper menuOfferDtoMapper;

  // Requests

  @Mapping(source = "placeCreateRequest.placeTypeIdentifier", target = "placeType.identifier")
  @Mapping(source = "requestingUser", target = "owner")
  @Mapping(source = "placeCreateRequest.enabled", target = "enabled")
  public abstract Place toDomain(PlaceCreateRequest placeCreateRequest, User requestingUser);

  @Mapping(source = "placeTypeIdentifier", target = "placeType.identifier")
  @Mapping(source = "ownerEmail", target = "owner.email")
  @Mapping(source = "enabled", target = "enabled")
  public abstract Place toDomain(PlaceUpdateRequest placeUpdateRequest);

  // Responses

  public abstract PlaceBasicResponse toBasicResponse(Place place);

  @Mapping(target = "menuOffers", ignore = true)
  public abstract PlaceFullResponse toPlaceFullResponse(Place place);

  @Mapping(source = "request", target = ".")
  @Mapping(source = "request.location", target = "location")
  @Mapping(source = "request.enabled", target = "enabled")
  public abstract PlaceSearchCommand toSearchRequest(PlaceSearchRequest request, User requestingUser);

  @AfterMapping
  protected void mapMenuOffers(@MappingTarget PlaceFullResponse response, Place place) {
    if (place.getMenuOffers() == null) {
      return;
    }
    response.setMenuOffers(place.getMenuOffers().stream().map(mo -> menuOfferDtoMapper.toDto(mo, place)).toList());
  }

  @AfterMapping
  protected void nullifyHavingNullFields(@MappingTarget PlaceSearchCommand searchRequest) {
    if (searchRequest.getLocation() != null && searchRequest.getLocation().getLatitude() == null
        && searchRequest.getLocation().getLongitude() == null
        && searchRequest.getLocation().getRadius() == null) {
      searchRequest.setLocation(null);
    }
    if (searchRequest.getOpenAt() != null && searchRequest.getOpenAt().getDay() == null
        && searchRequest.getOpenAt().getTime() == null) {
      searchRequest.setOpenAt(null);
    }
  }
}
