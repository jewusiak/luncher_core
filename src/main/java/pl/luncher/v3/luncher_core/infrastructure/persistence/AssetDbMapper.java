package pl.luncher.v3.luncher_core.infrastructure.persistence;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import pl.luncher.v3.luncher_core.assets.model.Asset;

@Mapper(componentModel = ComponentModel.SPRING, uses = {PlaceDbMapper.class,
    WeekDayTimeRangeMapper.class})
interface AssetDbMapper {


  @Mapping(target = "id", source = "asset.id")
  @Mapping(target = "description", source = "asset.description")
  AssetDb toDbEntity(Asset asset, PlaceDb place);


  Asset toDomain(AssetDb assetDb);

}
