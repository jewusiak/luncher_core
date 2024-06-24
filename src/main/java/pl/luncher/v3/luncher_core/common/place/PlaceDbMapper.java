package pl.luncher.v3.luncher_core.common.place;

import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminPlaceCreationRequest;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminUpdatePlaceRequest;
import pl.luncher.v3.luncher_core.admin.model.responses.AdminBasicPlaceResponse;
import pl.luncher.v3.luncher_core.admin.model.responses.AdminFullPlaceResponse;
import pl.luncher.v3.luncher_core.common.persistence.models.OpeningWindowDb;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceTypeDb;
import pl.luncher.v3.luncher_core.common.place.valueobject.OpeningWindowDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, unmappedSourcePolicy = ReportingPolicy.WARN, componentModel = ComponentModel.SPRING)
interface PlaceDbMapper {


  @Mapping(target = "id", ignore = true)
  @Mapping(target = "standardOpeningTimes", ignore = true)
  @Mapping(target = "name", source = "request.name")
  @Mapping(target = "images", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
      ignoreUnmappedSourceProperties = {"placeTypeIdentifier"})
  void updateWith(@MappingTarget PlaceDb placeDb, AdminUpdatePlaceRequest request,
      PlaceTypeDb placeType);

  @BeanMapping(ignoreByDefault = true)
  AdminBasicPlaceResponse mapToBasic(PlaceDb placeDb);


  AdminFullPlaceResponse mapToFull(PlaceDb placeDb);


  @Mapping(target = "place", ignore = true)
  OpeningWindowDb mapToDb(OpeningWindowDto openingWindow);

  @InheritInverseConfiguration
  @BeanMapping(ignoreUnmappedSourceProperties = "place")
  OpeningWindowDto mapToDto(OpeningWindowDb openingWindowDb);


  @Mapping(target = "id", ignore = true)
  @Mapping(target = "standardOpeningTimes", ignore = true)
  @Mapping(target = "name", source = "request.name")
  @BeanMapping(ignoreUnmappedSourceProperties = {"placeTypeIdentifier"})
  PlaceDb fromCreation(AdminPlaceCreationRequest request, PlaceTypeDb placeType);


}