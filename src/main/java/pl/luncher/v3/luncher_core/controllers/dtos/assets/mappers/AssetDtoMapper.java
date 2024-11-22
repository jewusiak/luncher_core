package pl.luncher.v3.luncher_core.controllers.dtos.assets.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.controllers.dtos.assets.responses.AssetFullResponse;
import pl.luncher.v3.luncher_core.controllers.dtos.common.AssetBasicResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface AssetDtoMapper {


  AssetFullResponse toAssetFullResponse(Asset asset);

  @Mapping(target = "mimeType", source = "mimeType.mimeType")
  AssetBasicResponse toAssetBasicResponse(Asset asset);
}
