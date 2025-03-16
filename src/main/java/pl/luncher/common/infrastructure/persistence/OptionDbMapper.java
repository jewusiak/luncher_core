package pl.luncher.common.infrastructure.persistence;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import pl.luncher.v3.luncher_core.place.model.menus.Option;

@Mapper(componentModel = ComponentModel.SPRING, uses = {MonetaryAmountDbMapper.class})
interface OptionDbMapper {

  OptionDb toDb(Option domainObject);

  Option toDomain(OptionDb dbEntity);
}
