package pl.luncher.v3.luncher_core.controllers.dtos.menus.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.controllers.dtos.common.mappers.MonetaryAmountMapper;
import pl.luncher.v3.luncher_core.controllers.dtos.menus.dtos.MenuOfferDto;
import pl.luncher.v3.luncher_core.controllers.dtos.menus.dtos.OptionDto;
import pl.luncher.v3.luncher_core.controllers.dtos.menus.dtos.PartDto;
import pl.luncher.v3.luncher_core.controllers.dtos.menus.requests.MenuOfferRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.menus.responses.MenuOfferFullResponse;
import pl.luncher.v3.luncher_core.place.model.menus.MenuOffer;
import pl.luncher.v3.luncher_core.place.model.menus.Option;
import pl.luncher.v3.luncher_core.place.model.menus.Part;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING, uses = {
    MonetaryAmountMapper.class})
public interface MenuOfferDtoMapper {

  // requests
  @Mapping(source = "menuOffer", target = ".")
  MenuOffer creationToDomain(MenuOfferRequest request);

  @Mapping(source = "menuOffer", target = ".")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateDomain(@MappingTarget MenuOffer menuOffer, MenuOfferRequest request);

  // responses
  MenuOfferFullResponse toMenuOfferFullResponse(MenuOffer menuOffer);

  // response dtos
  MenuOfferDto toDto(MenuOffer menuOffer);

  OptionDto toDto(Option option);

  PartDto toDto(Part part);

  MenuOffer toDomain(MenuOfferDto menuOffer);

  Option toDomain(OptionDto option);

  Part toDomain(PartDto part);

  @BeforeMapping
  default void stripIds(@MappingTarget MenuOffer menuOffer, MenuOfferRequest request) {
    MenuOfferDto offer = request.getMenuOffer();
    if (offer == null) {
      return;
    }
    offer.setId(null);
    if (offer.getParts() != null) {
      offer.getParts().forEach(p -> {
        p.setId(null);
        if (p.getOptions() != null) {
          p.getOptions().forEach(o -> o.setId(null));
        }
      });
    }
  }

}
