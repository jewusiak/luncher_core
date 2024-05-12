package pl.luncher.v3.luncher_core.common.persistence.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "place_types", schema = "luncher_core")
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class PlaceTypeDb {

  @Id
  private String identifier;

  private String iconName;

  private String name;
}
