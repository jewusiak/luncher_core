package pl.luncher.v3.luncher_core.infrastructure.persistence;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import pl.luncher.v3.luncher_core.place.model.menus.MenuOffer;

@Mapper(componentModel = ComponentModel.SPRING, uses = {MonetaryAmountDbMapper.class, OptionDbMapper.class,
    WeekDayTimeRangeMapper.class, PartDbMapper.class})
interface MenuOfferDbMapper {

  MenuOffer toDomain(MenuOfferDb dbEntity);

  @Mapping(target = "place", ignore = true)
  MenuOfferDb toDb(MenuOffer domainObject);

  @AfterMapping
  default void linkChildEntities(@MappingTarget MenuOfferDb dbEntity) {
    if (dbEntity.getParts() != null) {
      dbEntity.getParts().forEach(p -> p.setParentOffer(dbEntity));
    }
    if (dbEntity.getOneTimeServingRanges() != null) {
      dbEntity.getOneTimeServingRanges().forEach(r -> r.setMenuOffer(dbEntity));
    }
    if (dbEntity.getRecurringServingRanges() != null) {
      dbEntity.getRecurringServingRanges().forEach(r -> r.setMenuOffer(dbEntity));
    }
  }
}
