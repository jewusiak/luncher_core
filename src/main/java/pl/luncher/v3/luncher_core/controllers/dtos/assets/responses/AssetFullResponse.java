package pl.luncher.v3.luncher_core.controllers.dtos.assets.responses;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.assets.model.AssetUploadStatus;
import pl.luncher.v3.luncher_core.assets.model.MimeContentFileType;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.assets.model.Asset}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetFullResponse implements Serializable {

  private UUID id;
  private String description;
  private String originalFilename;
  private String storagePath;
  private String accessUrl;
  private UUID placeId;
  private UUID sectionElementId;
  private MimeContentFileType mimeType;
  private AssetUploadStatus uploadStatus;
  private String blurHash;
}
