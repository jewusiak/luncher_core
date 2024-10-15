package pl.luncher.v3.luncher_core.infrastructure.persistence;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import pl.luncher.v3.luncher_core.common.model.MonetaryAmount;

@Mapper(componentModel = ComponentModel.SPRING)
interface MonetaryAmountDbMapper {

  MonetaryAmountDb toDb(MonetaryAmount entity);

  default MonetaryAmount toDomain(MonetaryAmountDb dbEntity) {
    if (dbEntity == null) {
      return null;
    }
    return MonetaryAmount.of(dbEntity.getAmount(), dbEntity.getCurrencyCode());
  }
}
