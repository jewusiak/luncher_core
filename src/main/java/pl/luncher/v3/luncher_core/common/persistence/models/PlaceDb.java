package pl.luncher.v3.luncher_core.common.persistence.models;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import pl.luncher.v3.luncher_core.common.domain.place.valueobject.Address;
import pl.luncher.v3.luncher_core.common.model.dto.Location;
import pl.luncher.v3.luncher_core.common.persistence.LocationToPointPersistenceConverter;


@Entity
@Builder(access = AccessLevel.PACKAGE)
@Table(name = "places", schema = "luncher_core")
@Indexed(index = "places")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class PlaceDb {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @FullTextField
  private String name;
  @FullTextField
  private String longName;
  @FullTextField
  private String description;

  private String facebookPageId;

  private String instagramHandle;

  private String webpageUrl;

  private String phoneNumber;

  @Embedded
  @IndexedEmbedded
  private Address address;
  private String googleMapsReference;

  @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
  @JoinTable(schema = "luncher_core")
  @IndexedEmbedded
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
  private List<OpeningWindowDb> standardOpeningTimes;
//todo: not mvp
//  @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
//  private List<PlaceOpeningException> openingExceptions;

  @ManyToOne
  @IndexedEmbedded
  private PlaceTypeDb placeType;

  @Column(columnDefinition = "geography(Point, 4326)")
  @Convert(converter = LocationToPointPersistenceConverter.class)
  @GenericField
  private Location location;

  @ManyToOne
  private UserDb owner;

  @OneToMany(cascade = {
      CascadeType.ALL}, orphanRemoval = true, mappedBy = "refToPlaceImages", fetch = FetchType.EAGER)
  private List<AssetDb> images;


  public void addStandardOpeningTime(OpeningWindowDb openingWindowDb) {
    if (standardOpeningTimes == null) {
      standardOpeningTimes = new ArrayList<>();
    }

    standardOpeningTimes.add(openingWindowDb);
  }
}
