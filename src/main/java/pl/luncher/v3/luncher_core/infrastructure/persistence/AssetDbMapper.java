package pl.luncher.v3.luncher_core.infrastructure.persistence;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.beans.factory.annotation.Autowired;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.configuration.properties.LuncherProperties;

@Mapper(componentModel = ComponentModel.SPRING, uses = {WeekDayTimeRangeMapper.class})
abstract class AssetDbMapper {

  @Autowired
  protected LuncherProperties luncherProperties;

  @Mapping(target = "id", source = "asset.id")
  @Mapping(target = "description", source = "asset.description")
  @Mapping(target = "place", source = "place")
  @Mapping(target = "sectionElements", ignore = true)
  public abstract AssetDb toDbEntity(Asset asset, PlaceDb place);


  @Mapping(target = "placeId", source = "place.id")
  @Mapping(target = "sectionElementIds", expression = "java(java.util.Optional.ofNullable(assetDb.getSectionElements()).stream().flatMap(java.util.List::stream).map(SectionElementDb::getId).toList())")
  @Mapping(target = "accessUrl", expression = "java(\"%s/asset/%s\".formatted(luncherProperties.getBaseApiUrl(), assetDb.getId()))")
  public abstract Asset toDomain(AssetDb assetDb);

}
