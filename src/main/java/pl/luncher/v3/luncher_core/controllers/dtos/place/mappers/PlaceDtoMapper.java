package pl.luncher.v3.luncher_core.controllers.dtos.place.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.controllers.dtos.common.mappers.WeekDayTimeRangeDtoMapper;
import pl.luncher.v3.luncher_core.controllers.dtos.menus.mappers.MenuOfferDtoMapper;
import pl.luncher.v3.luncher_core.controllers.dtos.place.requests.PlaceCreateRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.place.requests.PlaceSearchRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.place.requests.PlaceUpdateRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.place.responses.PlaceBasicResponse;
import pl.luncher.v3.luncher_core.controllers.dtos.place.responses.PlaceFullResponse;
import pl.luncher.v3.luncher_core.place.domainservices.PlaceSearchService.SearchRequest;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.user.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING, uses = {
    WeekDayTimeRangeDtoMapper.class,
    MenuOfferDtoMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PlaceDtoMapper {

  // Requests

  @Mapping(source = "placeCreateRequest.placeTypeIdentifier", target = "placeType.identifier")
  @Mapping(source = "requestingUser.email", target = "owner.email")
  Place toDomain(PlaceCreateRequest placeCreateRequest, User requestingUser);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(source = "placeTypeIdentifier", target = "placeType.identifier")
  @Mapping(target = "id", ignore = true)
  Place updateDomain(
      PlaceUpdateRequest placeUpdateRequest, @MappingTarget Place place);

  // Responses

  PlaceBasicResponse toBasicResponse(Place place);

  PlaceFullResponse toPlaceFullResponse(Place place);

  @Mapping(source = "location", target = "location")
  SearchRequest toSearchRequest(PlaceSearchRequest request);
}
