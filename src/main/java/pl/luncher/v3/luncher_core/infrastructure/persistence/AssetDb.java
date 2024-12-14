package pl.luncher.v3.luncher_core.infrastructure.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.luncher.v3.luncher_core.assets.model.AssetUploadStatus;
import pl.luncher.v3.luncher_core.assets.model.MimeContentFileType;

@Entity
@Table(name = "assets", schema = "luncher_core")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
class AssetDb {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String description;
  private String originalFilename;
  private String storagePath;
  @Enumerated(EnumType.STRING)
  private MimeContentFileType mimeType;
  @Enumerated(EnumType.STRING)
  private AssetUploadStatus uploadStatus;

  @ManyToOne
  private PlaceDb place;

  @OneToMany(mappedBy = "thumbnail", cascade = CascadeType.REMOVE)
  private List<SectionElementDb> sectionElements;

  private int placeImageIdx;

  private String blurHash;

  public void insertPlaceListIndexValue() {
    if (place == null) {
      return;
    }
    if (place.getImages() == null) {
      return;
    }
    if (this.id == null || place.getImages().stream()
        .noneMatch(img -> img.getId().equals(this.id))) {
      placeImageIdx = place.getImages().stream().map(AssetDb::getPlaceImageIdx)
          .max(Integer::compareTo).orElse(-1) + 1;
    }
  }
}
