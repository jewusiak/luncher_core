package pl.luncher.v3.luncher_core.place.model.menus;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.interfaces.Validatable;
import pl.luncher.v3.luncher_core.common.model.MonetaryAmount;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Option implements Validatable {

  private UUID id;
  private String name;
  private String description;
  private MonetaryAmount supplement;


  @Override
  public void validate() {

  }
}
