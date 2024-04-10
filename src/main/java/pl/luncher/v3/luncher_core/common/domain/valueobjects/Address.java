package pl.luncher.v3.luncher_core.common.domain.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

@Data
@Builder
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
@Indexed
public class Address {
    @FullTextField
    private String firstLine;
    @FullTextField
    private String secondLine;
    @FullTextField
    private String zipCode;
    @FullTextField
    private String city;
    @FullTextField
    private String district;
    @KeywordField
    private String country; //ISO
}
