package pl.luncher.v3.luncher_core.infrastructure.persistence;

import java.util.ArrayList;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import pl.luncher.v3.luncher_core.assets.model.Asset;

@Mapper(componentModel = ComponentModel.SPRING, uses = {WeekDayTimeRangeMapper.class})
interface AssetDbMapper {


  @Mapping(target = "id", source = "asset.id")
  @Mapping(target = "description", source = "asset.description")
  @Mapping(target = "place", source = "place")
  AssetDb toDbEntity(Asset asset, PlaceDb place);


  @Mapping(target = "placeId", source = "place.id")
  Asset toDomain(AssetDb assetDb);
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
