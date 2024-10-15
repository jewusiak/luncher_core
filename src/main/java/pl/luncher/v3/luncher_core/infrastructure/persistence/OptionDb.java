package pl.luncher.v3.luncher_core.infrastructure.persistence;


import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "menu_offer_part_options", schema = "luncher_core")
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PACKAGE)
@Getter
@Setter
class OptionDb {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String name;
  private String description;
  @Embedded
  private MonetaryAmountDb supplement;
  private boolean required;

  @ManyToOne
  private PartDb parentPart;
}
