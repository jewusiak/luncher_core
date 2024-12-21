package pl.luncher.v3.luncher_core.application.controllers.dtos.menus.dtos;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.application.controllers.dtos.common.MonetaryAmountDto;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.place.model.menus.Part}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartDto implements Serializable {

  private UUID id;
  private String name;
  private boolean required;
  private MonetaryAmountDto supplement;
  private List<OptionDto> options;
}
