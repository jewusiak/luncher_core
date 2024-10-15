package pl.luncher.v3.luncher_core.infrastructure.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "menu_parts", schema = "luncher_core")
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PACKAGE)
@Getter
@Setter
class PartDb {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String name;
  private boolean required;
  @Embedded
  private MonetaryAmountDb supplement;

  @ManyToOne
  private MenuOfferDb parentOffer;

  @OneToMany(mappedBy = "parentPart", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<OptionDb> options;
}
