package pl.luncher.common.infrastructure.persistence;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import pl.luncher.v3.luncher_core.place.model.menus.Part;

@Mapper(componentModel = ComponentModel.SPRING, uses = {MonetaryAmountDbMapper.class, OptionDbMapper.class})
interface PartDbMapper {

  PartDb toDb(Part domainObject);

  Part toDomain(PartDb dbEntity);
}
