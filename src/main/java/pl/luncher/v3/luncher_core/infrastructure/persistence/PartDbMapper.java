package pl.luncher.v3.luncher_core.infrastructure.persistence;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import pl.luncher.v3.luncher_core.place.model.menus.Part;

@Mapper(componentModel = ComponentModel.SPRING, uses = {MonetaryAmountDbMapper.class, OptionDbMapper.class})
interface PartDbMapper {

  PartDb toDb(Part domainObject);

  Part toDomain(PartDb dbEntity);

  @AfterMapping
  default void linkChildEntities(@MappingTarget PartDb dbEntity) {
    if (dbEntity.getOptions() != null) {
      dbEntity.getOptions().forEach(o -> o.setParentPart(dbEntity));
    }
  }
}
