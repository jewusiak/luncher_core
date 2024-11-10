package pl.luncher.v3.luncher_core.controllers.dtos.menus.dtos;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.controllers.dtos.common.MonetaryAmountDto;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.place.model.menus.Option}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionDto implements Serializable {

  private UUID id;
  private String name;
  private String description;
  private MonetaryAmountDto supplement;
}
