package pl.luncher.v3.luncher_core.place.domainservices;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pl.luncher.v3.luncher_core.place.model.Place;

@Mapper(componentModel = "spring")
interface PlaceUpdateMapper {

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "images", ignore = true)
  @Mapping(source = "changes.enabled", target = "enabled")
  void updateDomain(@MappingTarget Place oldPlace, Place changes);

}
