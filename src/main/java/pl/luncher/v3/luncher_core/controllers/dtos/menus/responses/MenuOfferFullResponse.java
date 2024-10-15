package pl.luncher.v3.luncher_core.controllers.dtos.menus.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.controllers.dtos.menus.dtos.MenuOfferDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuOfferFullResponse {

  private MenuOfferDto menuOffer;
}
