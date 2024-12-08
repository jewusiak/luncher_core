package pl.luncher.v3.luncher_core.infrastructure.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Transient;
import pl.luncher.v3.luncher_core.contentmanagement.model.ElementType;
import pl.luncher.v3.luncher_core.contentmanagement.model.SectionElement;

/**
 * DTO for {@link SectionElement}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "page_arrangement_section_elements", schema = "luncher_core")
@ToString(exclude = {"section", "place", "placeType", "thumbnail"})
class SectionElementDb implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private int listIndex;
  private ElementType elementType;
  private String header;
  private String subheader;
  private String customUri;

  @ManyToOne(cascade = {CascadeType.MERGE})
  private AssetDb thumbnail;

  @ManyToOne
  private PlaceDb place;

  @ManyToOne
  private PlaceTypeDb placeType;

  @ManyToOne(optional = false)
  private SectionDb section;

  @Transient
  public String getSourceElementId() {
    return Optional.ofNullable(elementType).map(type -> switch (type) {
      case PLACE -> place == null ? null : place.getId();
      case PLACE_TYPE -> placeType == null ? null : placeType.getIdentifier();
      case OTHER -> null;
    }).map(Object::toString).orElse(null);
  }
}
