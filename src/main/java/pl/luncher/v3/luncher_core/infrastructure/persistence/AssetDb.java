package pl.luncher.v3.luncher_core.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
  private String storagePath;
  private String accessUrl;
  private MimeContentFileType mimeType;
  private AssetUploadStatus uploadStatus;

  @ManyToOne
  private PlaceDb place;
}
