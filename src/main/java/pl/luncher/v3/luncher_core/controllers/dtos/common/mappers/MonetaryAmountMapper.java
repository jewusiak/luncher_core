package pl.luncher.v3.luncher_core.controllers.dtos.common.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import pl.luncher.v3.luncher_core.common.model.MonetaryAmount;
import pl.luncher.v3.luncher_core.controllers.dtos.common.MonetaryAmountDto;

@Mapper(componentModel = ComponentModel.SPRING)
public interface MonetaryAmountMapper {

  MonetaryAmountDto toDto(MonetaryAmount monetaryAmount);

  MonetaryAmount toDomain(MonetaryAmountDto monetaryAmount);
}
