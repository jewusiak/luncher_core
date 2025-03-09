package pl.luncher.common.infrastructure.persistence;

import jakarta.persistence.CascadeType;
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
import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.AssociationInverseSide;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue;

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

  @OrderBy("listIdx")
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinTable(name = "menu_offer_parts", schema = "luncher_core", joinColumns = @JoinColumn(name = "menu_offer_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "part_id", referencedColumnName = "id"))
  private List<PartDb> parts;

  @IndexedEmbedded(structure = ObjectStructure.NESTED)
  @AssociationInverseSide(inversePath = @ObjectPath(@PropertyValue(propertyName = "menuOffer")))
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(name = "menu_offer_recurring_serving_ranges", schema = "luncher_core", joinColumns = @JoinColumn(name = "menu_offer_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "weekdaytimerange_id", referencedColumnName = "id"))
  private List<WeekDayTimeRangeDb> recurringServingRanges;

  @IndexedEmbedded(structure = ObjectStructure.NESTED)
  @AssociationInverseSide(inversePath = @ObjectPath(@PropertyValue(propertyName = "menuOffer")))
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(name = "menu_offer_onetime_serving_ranges", schema = "luncher_core", joinColumns = @JoinColumn(name = "menu_offer_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "localdatetimerange_id", referencedColumnName = "id"))
  private List<LocalDateTimeRangeDb> oneTimeServingRanges;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinTable(name = "place_menu_offers", schema = "luncher_core",
      inverseJoinColumns = @JoinColumn(name = "place_id", referencedColumnName = "id"),
      joinColumns = @JoinColumn(name = "menu_offer_id", referencedColumnName = "id"))
  private PlaceDb place;

}
