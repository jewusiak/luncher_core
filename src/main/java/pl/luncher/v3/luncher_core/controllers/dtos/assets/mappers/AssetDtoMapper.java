package pl.luncher.v3.luncher_core.controllers.dtos.assets.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.configuration.properties.LuncherProperties;
import pl.luncher.v3.luncher_core.controllers.dtos.assets.responses.AssetFullResponse;
import pl.luncher.v3.luncher_core.controllers.dtos.common.AssetBasicResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public abstract class AssetDtoMapper {

  @Autowired
  protected LuncherProperties luncherProperties;

  @Mapping(target = "accessUrl", expression = "java(asset.getAccessUrl() == null ? null : (luncherProperties.getBaseApiUrl()+asset.getAccessUrl()))")
  public abstract AssetFullResponse toAssetFullResponse(Asset asset);

  @Mapping(target = "mimeType", source = "mimeType.mimeType")
  @Mapping(target = "accessUrl", expression = "java(asset.getAccessUrl() == null ? null : (luncherProperties.getBaseApiUrl()+asset.getAccessUrl()))")
  public abstract AssetBasicResponse toAssetBasicResponse(Asset asset);
}
