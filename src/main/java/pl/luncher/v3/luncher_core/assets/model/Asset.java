package pl.luncher.v3.luncher_core.assets.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.assets.domainservices.AssetPermissionsChecker;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {

  private UUID id;
  private String description;
  private String storagePath;
  private String accessUrl;
  private UUID placeId;
  private UUID placeOwnerId;
  private MimeContentFileType mimeType;
  private AssetUploadStatus uploadStatus;

  public void validate() {

  }

  public AssetPermissionsChecker permissions() {
    return new AssetPermissionsChecker(this);
  }
}
