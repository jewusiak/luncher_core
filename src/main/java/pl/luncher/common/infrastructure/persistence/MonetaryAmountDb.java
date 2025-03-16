package pl.luncher.common.infrastructure.persistence;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.ToString;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

@Embeddable
@Data
@ToString
class MonetaryAmountDb {

  @GenericField
  private BigDecimal amount;
  private String currencyCode;
}
