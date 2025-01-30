package pl.luncher.v3.luncher_core.infrastructure.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import lombok.ToString;
import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

@Entity
@Table(name = "menu_offers", schema = "luncher_core")
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PACKAGE)
@Getter
@Setter
@ToString(exclude = {"place"})
class MenuOfferDb {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String name;

  @IndexedEmbedded
  @Embedded
  private MonetaryAmountDb basePrice;

  @OneToMany(mappedBy = "parentOffer", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<PartDb> parts;

  @IndexedEmbedded(structure = ObjectStructure.NESTED)
  @OneToMany(mappedBy = "menuOffer", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<WeekDayTimeRangeDb> recurringServingRanges;

  @IndexedEmbedded(structure = ObjectStructure.NESTED)
  @OneToMany(mappedBy = "menuOffer", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<LocalDateTimeRangeDb> oneTimeServingRanges;

  @ManyToOne(fetch = FetchType.LAZY)
  private PlaceDb place;

}
