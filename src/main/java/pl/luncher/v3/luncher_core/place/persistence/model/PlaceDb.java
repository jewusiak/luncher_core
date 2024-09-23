package pl.luncher.v3.luncher_core.place.persistence.model;


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
import pl.luncher.v3.luncher_core.common.persistence.LocationToPointPersistenceConverter;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceTypeDb;
import pl.luncher.v3.luncher_core.common.persistence.models.UserDb;


@Entity
@Builder(access = AccessLevel.PACKAGE)
@Table(name = "places", schema = "luncher_core")
@Indexed(index = "places")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(exclude = {"owner", "images"})
public class PlaceDb {

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
  @IndexedEmbedded
  private AddressDb address;
  private String googleMapsReference;

  @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "place")
  @IndexedEmbedded(structure = ObjectStructure.NESTED)
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
  private List<OpeningWindowDb> openingWindows;
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
  private UserDb owner;

  @OneToMany(cascade = {
      CascadeType.ALL}, orphanRemoval = true, mappedBy = "place", fetch = FetchType.EAGER)
  private List<AssetDb> images;

}