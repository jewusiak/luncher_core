package pl.luncher.v3.luncher_core.application.controllers.dtos.assets.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.application.controllers.dtos.assets.responses.AssetFullResponse;
import pl.luncher.v3.luncher_core.application.controllers.dtos.common.AssetBasicResponse;
import pl.luncher.v3.luncher_core.assets.model.Asset;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING, nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface AssetDtoMapper {


  AssetFullResponse toAssetFullResponse(Asset asset);

  @Mapping(target = "mimeType", source = "mimeType.mimeType")
  AssetBasicResponse toAssetBasicResponse(Asset asset);
}
