package pl.luncher.v3.luncher_core.infrastructure.persistence;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;


@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class AddressDb {

  @FullTextField
  private String firstLine;
  @FullTextField
  private String secondLine;
  private String zipCode;
  @FullTextField
  private String city;
  @FullTextField
  private String district;

  @FullTextField
  private String description;

  @KeywordField
  private String country; //ISO

}
