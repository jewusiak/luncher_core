package pl.luncher.v3.luncher_core.presentation.controllers.dtos.place.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.common.domain.users.User;
import pl.luncher.v3.luncher_core.place.domainservices.PlaceSearchService.SearchRequest;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.common.mappers.OpeningWindowDtoMapper;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.place.requests.PlaceCreateRequest;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.place.requests.PlaceSearchRequest;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.place.requests.PlaceUpdateRequest;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.place.responses.PlaceBasicResponse;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.place.responses.PlaceFullResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING, uses = {
    OpeningWindowDtoMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PlaceConcerningDtoMapper {

  // Requests

  @Mapping(source = "placeCreateRequest.placeTypeIdentifier", target = "placeType.identifier")
  @Mapping(source = "requestingUser.dbEntity.email", target = "owner.email")
  Place toDomain(PlaceCreateRequest placeCreateRequest, User requestingUser);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(source = "placeTypeIdentifier", target = "placeType.identifier")
  Place updateDomain(
      PlaceUpdateRequest placeUpdateRequest, @MappingTarget Place place);

  // Responses

  PlaceBasicResponse toBasicResponse(Place place);

  PlaceFullResponse toPlaceFullResponse(Place place);

  @Mapping(source = "location", target = "location")
  SearchRequest toSearchRequest(PlaceSearchRequest request);
}
