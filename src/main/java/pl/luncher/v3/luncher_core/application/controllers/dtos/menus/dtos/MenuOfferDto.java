package pl.luncher.v3.luncher_core.application.controllers.dtos.menus.dtos;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.application.controllers.dtos.common.LocalDateTimeRangeDto;
import pl.luncher.v3.luncher_core.application.controllers.dtos.common.MonetaryAmountDto;
import pl.luncher.v3.luncher_core.application.controllers.dtos.common.WeekDayTimeRangeDto;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.place.model.menus.MenuOffer}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuOfferDto implements Serializable {

  private UUID id;
  private String name;
  private MonetaryAmountDto basePrice;
  private List<PartDto> parts;
  private List<WeekDayTimeRangeDto> recurringServingRanges;
  private List<LocalDateTimeRangeDto> oneTimeServingRanges;
  private LocalDateTimeRangeDto thisOrNextServingRange;
  private String sourceUrl;
  private boolean beingServed;
}
