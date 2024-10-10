package pl.luncher.v3.luncher_core.controllers.dtos.assets.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.controllers.dtos.assets.responses.AssetFullResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface AssetDtoMapper {

  AssetFullResponse toAssetFullResponse(Asset asset);

}
