package pl.luncher.common.infrastructure.persistence;

import java.util.Comparator;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import pl.luncher.v3.luncher_core.common.model.timing.LocalDateTimeRange;
import pl.luncher.v3.luncher_core.common.model.timing.WeekDayTimeRange;
import pl.luncher.v3.luncher_core.place.model.menus.MenuOffer;

@Mapper(componentModel = ComponentModel.SPRING, uses = {MonetaryAmountDbMapper.class, OptionDbMapper.class,
    WeekDayTimeRangeMapper.class, PartDbMapper.class})
interface MenuOfferDbMapper {

  MenuOffer toDomain(MenuOfferDb dbEntity);

  MenuOfferDb toDb(MenuOffer domainObject);


  @AfterMapping
  default void sortServingTimes(@MappingTarget MenuOffer domainObject) {
    if (domainObject.getRecurringServingRanges() != null) {
      domainObject.getRecurringServingRanges()
          .sort(Comparator.comparing(WeekDayTimeRange::getStartTime));
    }
    if (domainObject.getOneTimeServingRanges() != null) {
      domainObject.getOneTimeServingRanges()
          .sort(Comparator.comparing(LocalDateTimeRange::getStartTime));
    }
  }
}
