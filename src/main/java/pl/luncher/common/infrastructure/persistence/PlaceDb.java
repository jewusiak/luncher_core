package pl.luncher.common.infrastructure.persistence;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import java.time.ZoneId;
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
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.AssociationInverseSide;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue;


@Entity
@Builder(access = AccessLevel.PACKAGE)
@Table(name = "places", schema = "luncher_core")
@Indexed(index = "places")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(exclude = {"owner", "images"})
class PlaceDb {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @FullTextField(analyzer = "names_analyzer")
  private String name;
  @FullTextField(analyzer = "morfologik_polish")
  private String longName;
  @FullTextField(analyzer = "morfologik_polish")
  private String description;

  private String facebookPageId;

  private String instagramHandle;

  private String webpageUrl;

  private String phoneNumber;

  @Embedded
  @IndexedEmbedded(structure = ObjectStructure.NESTED)
  private AddressDb address;
  private String googleMapsReference;

  @IndexedEmbedded(structure = ObjectStructure.NESTED)
  @AssociationInverseSide(inversePath = @ObjectPath(@PropertyValue(propertyName = "place")))
  @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinTable(name = "place_opening_windows",
      schema = "luncher_core", joinColumns = @JoinColumn(name = "place_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "weekdaytimerange_id", referencedColumnName = "id"))
  private List<WeekDayTimeRangeDb> openingWindows;
//todo: not mvp
//  @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
//  private List<PlaceOpeningException> openingExceptions;

  @ManyToOne
  @IndexedEmbedded
  private PlaceTypeDb placeType;

  @Column(columnDefinition = "geography(Point, 4326)")
  @Convert(converter = LocationToPointPersistenceConverter.class)
  @GenericField
  private LocationDb location;

  @ManyToOne
  @IndexedEmbedded(structure = ObjectStructure.NESTED, includePaths = {"uuid"})
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
  private UserDb owner;

  @GenericField
  private Boolean enabled;
  
  @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "place", fetch = FetchType.EAGER)
  @OrderBy("placeImageIdx")
  private List<AssetDb> images;

  @IndexedEmbedded(structure = ObjectStructure.NESTED)
  @AssociationInverseSide(inversePath = @ObjectPath(@PropertyValue(propertyName = "place")))
  @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
  @JoinTable(name = "place_menu_offers", schema = "luncher_core",
      joinColumns = @JoinColumn(name = "place_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "menu_offer_id", referencedColumnName = "id"))
  private List<MenuOfferDb> menuOffers;

  private ZoneId timeZone;
}
