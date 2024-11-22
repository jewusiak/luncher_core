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
  public abstract AssetDb toDbEntity(Asset asset, PlaceDb place);


  @Mapping(target = "placeId", source = "place.id")
  @Mapping(target = "accessUrl", expression = "java(\"%s/asset/%s\".formatted(luncherProperties.getBaseApiUrl(), assetDb.getId()))")
  public abstract Asset toDomain(AssetDb assetDb);
//
//  @AfterMapping
//  default void ensurePlaceContainsAsset(@MappingTarget AssetDb assetDb) {
//    if (assetDb.getPlace() == null) {
//      return;
//    }
//    if (assetDb.getPlace().getImages() == null) {
//      assetDb.getPlace().setImages(new ArrayList<>());
//    }
//    if (assetDb.getId() == null) {
//      // asset hasn't been persisted yet
//      assetDb.getPlace().getImages().add(assetDb);
//      return;
//    }
//    // else - asset existed before but we ensure its linked to place correctly
//    assetDb.getPlace().getImages().removeIf(i -> i.getId().equals(assetDb.getId()));
//    assetDb.getPlace().getImages().add(assetDb);
//  }
}
