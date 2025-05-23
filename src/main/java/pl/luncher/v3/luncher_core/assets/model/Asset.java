package pl.luncher.v3.luncher_core.assets.model;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {

  private UUID id;
  private String description;
  private String originalFilename;
  private String storagePath;
  private String accessUrl;
  private UUID placeId;
  private List<UUID> sectionElementIds;
  private MimeContentFileType mimeType;
  private AssetUploadStatus uploadStatus;
  private byte[] content;
  private String blurHash;

  public void validate() {

  }
}
