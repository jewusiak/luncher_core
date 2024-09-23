package pl.luncher.v3.luncher_core.place.persistence.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
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
public class AddressDb {

  private String firstLine;
  private String secondLine;
  private String zipCode;
  private String city;
  private String district;

  @FullTextField
  private String description;

  @KeywordField
  private String country; //ISO

  @Transient
  @FullTextField
  private String addressString() {
    return """
        %s
        %s
        %s, %s %s""".formatted(firstLine, secondLine, zipCode, city, district);
  }
}
