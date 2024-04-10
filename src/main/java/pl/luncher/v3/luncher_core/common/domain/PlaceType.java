package pl.luncher.v3.luncher_core.common.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "place_types", schema = "luncher_core")
public class PlaceType {
    @Id
    private String identifier;

    private String iconName;

    private String name;
}
