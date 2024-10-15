package pl.luncher.v3.luncher_core.controllers.dtos.menus.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.controllers.dtos.menus.dtos.MenuOfferDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuOfferRequest {

  @NotNull
  MenuOfferDto menuOffer;
}
