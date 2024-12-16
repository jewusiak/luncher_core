package pl.luncher.v3.luncher_core.controllers.dtos.menus.mappers;

import java.time.LocalDateTime;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.controllers.dtos.common.mappers.MonetaryAmountMapper;
import pl.luncher.v3.luncher_core.controllers.dtos.menus.dtos.MenuOfferDto;
import pl.luncher.v3.luncher_core.controllers.dtos.menus.dtos.OptionDto;
import pl.luncher.v3.luncher_core.controllers.dtos.menus.dtos.PartDto;
import pl.luncher.v3.luncher_core.place.model.menus.MenuOffer;
import pl.luncher.v3.luncher_core.place.model.menus.Option;
import pl.luncher.v3.luncher_core.place.model.menus.Part;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING, uses = {
    MonetaryAmountMapper.class})
public interface MenuOfferDtoMapper {

  // response dtos
  @Mapping(target = "beingServed", qualifiedByName = "mapBeingServed", source = "menuOffer")
  MenuOfferDto toDto(MenuOffer menuOffer);

  OptionDto toDto(Option option);

  PartDto toDto(Part part);

  MenuOffer toDomain(MenuOfferDto menuOffer);

  Option toDomain(OptionDto option);

  Part toDomain(PartDto part);

  @BeforeMapping
  default void stripIds(@MappingTarget MenuOffer menuOffer, MenuOfferDto offer) {
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

  @Named("mapBeingServed")
  default boolean mapBeingServed(MenuOffer menuOffer) {
    return menuOffer.isBeingServed(LocalDateTime.now());
  }

}
