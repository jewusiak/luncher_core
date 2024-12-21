package pl.luncher.v3.luncher_core.application.controllers.dtos.common;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.common.model.MonetaryAmount}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonetaryAmountDto implements Serializable {

  private BigDecimal amount;
  private String currencyCode;
}
