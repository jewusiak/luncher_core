package pl.luncher.v3.luncher_core.place.model.menus;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.interfaces.Validatable;
import pl.luncher.v3.luncher_core.common.model.MonetaryAmount;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Part implements Validatable {

  private UUID id;
  private String name;
  private MonetaryAmount supplement;

  private List<Option> options;

  @Override
  public void validate() {
    options.forEach(Validatable::validate);
  }
}
