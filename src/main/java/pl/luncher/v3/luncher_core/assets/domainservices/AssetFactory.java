package pl.luncher.v3.luncher_core.assets.domainservices;

import java.util.UUID;
import pl.luncher.v3.luncher_core.assets.domainservices.exceptions.CannotEstablishFileTypeException;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.assets.model.AssetUploadStatus;
import pl.luncher.v3.luncher_core.assets.model.MimeContentFileType;


public class AssetFactory {

  public static Asset newFilesystemPersistent(String description, String originalFilename,
      String originalFileContentType, UUID placeId) {
    MimeContentFileType fileType = MimeContentFileType.fromFilename(originalFilename);

    if (fileType == null) {
      fileType = MimeContentFileType.byMimeType(originalFileContentType);
    }

    if (fileType == null) {
      throw new CannotEstablishFileTypeException();
    }

    return Asset.builder().description(description).uploadStatus(AssetUploadStatus.AWAITING)
        .placeId(placeId)
        .mimeType(fileType)
        .originalFilename(originalFilename)
        .build();
  }
}
