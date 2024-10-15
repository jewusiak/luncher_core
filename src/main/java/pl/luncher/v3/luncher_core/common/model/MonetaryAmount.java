package pl.luncher.v3.luncher_core.common.model;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class MonetaryAmount {

  private BigDecimal amount;
  private String currencyCode;

  public static MonetaryAmount of(@NotNull BigDecimal amount, @NotNull @Length(min = 3, max = 3) String currencyCode) {
    return new MonetaryAmount(amount, currencyCode.toUpperCase());
  }
}
