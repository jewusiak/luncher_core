package pl.luncher.v3.luncher_core.common.persistence.models;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import pl.luncher.v3.luncher_core.common.place.valueobject.Address;


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

  private String googleMapsPlaceId;

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
  private List<OpeningWindowDb> standardOpeningTimes;
//todo: not mvp
//  @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
//  private List<PlaceOpeningException> openingExceptions;

  @ManyToOne
  private PlaceTypeDb placeType;

  @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "refToPlaceImages")
  private List<ImageAssetDb> images;


  public void addStandardOpeningTime(OpeningWindowDb openingWindowDb) {
    if (standardOpeningTimes == null) {
      standardOpeningTimes = new ArrayList<>();
    }

    standardOpeningTimes.add(openingWindowDb);
  }
}
