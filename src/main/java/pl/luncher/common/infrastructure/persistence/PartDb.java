package pl.luncher.common.infrastructure.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "menu_parts", schema = "luncher_core")
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PACKAGE)
@Getter
@Setter
@ToString(exclude = {"parentOffer"})
class PartDb {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column(length = 500)
  private String name;
  @Column(columnDefinition = "integer not null default 0")
  private int listIdx;
  @Embedded
  private MonetaryAmountDb supplement;

  @ManyToOne
  @JoinTable(name = "menu_offer_parts", schema = "luncher_core", inverseJoinColumns = @JoinColumn(name = "menu_offer_id", referencedColumnName = "id"), joinColumns = @JoinColumn(name = "part_id", referencedColumnName = "id"))
  private MenuOfferDb parentOffer;

  @OrderBy("listIdx")
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinTable(name = "menu_part_options",
      schema = "luncher_core", joinColumns = @JoinColumn(name = "part_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "option_id", referencedColumnName = "id"))
  private List<OptionDb> options;
}
