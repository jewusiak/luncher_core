package pl.luncher.v3.luncher_core.infrastructure.persistence;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;


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

  @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "place")
  @IndexedEmbedded(structure = ObjectStructure.NESTED)
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
  private List<WeekDayTimeRangeDb> openingWindows;
//todo: not mvp
//  @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
//  private List<PlaceOpeningException> openingExceptions;

  @ManyToOne
  @IndexedEmbedded
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
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

  @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "place")
  @IndexedEmbedded(structure = ObjectStructure.NESTED)
  private List<MenuOfferDb> menuOffers;
}
