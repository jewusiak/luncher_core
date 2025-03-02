package pl.luncher.v3.luncher_core.application.controllers.dtos.menus.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import pl.luncher.v3.luncher_core.application.controllers.dtos.common.mappers.MonetaryAmountMapper;
import pl.luncher.v3.luncher_core.application.controllers.dtos.menus.dtos.MenuOfferDto;
import pl.luncher.v3.luncher_core.application.controllers.dtos.menus.dtos.OptionDto;
import pl.luncher.v3.luncher_core.application.controllers.dtos.menus.dtos.PartDto;
import pl.luncher.v3.luncher_core.common.interfaces.LocalDateTimeProvider;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.place.model.menus.MenuOffer;
import pl.luncher.v3.luncher_core.place.model.menus.Option;
import pl.luncher.v3.luncher_core.place.model.menus.Part;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING, uses = {
    MonetaryAmountMapper.class}, nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class MenuOfferDtoMapper {

  @Autowired
  private LocalDateTimeProvider localDateTimeProvider;

  // response dtos
  @Mapping(target = "beingServed", ignore = true)
  public abstract MenuOfferDto toDto(MenuOffer menuOffer, @Context Place place);

  public abstract OptionDto toDto(Option option);

  public abstract PartDto toDto(Part part);

  public abstract MenuOffer toDomain(MenuOfferDto menuOffer);

  public abstract Option toDomain(OptionDto option);

  public abstract Part toDomain(PartDto part);

  @BeforeMapping
  void stripIds(@MappingTarget MenuOffer menuOffer, MenuOfferDto offer) {
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

  @AfterMapping
  void mapBeingServed(@MappingTarget MenuOfferDto dto, MenuOffer menuOffer, @Context Place place) {
    dto.setBeingServed(menuOffer.isBeingServed(localDateTimeProvider.now(place.getTimeZone())));
  }

}
