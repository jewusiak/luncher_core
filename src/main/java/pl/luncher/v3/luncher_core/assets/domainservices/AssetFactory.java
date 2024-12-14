package pl.luncher.v3.luncher_core.assets.domainservices;

import io.trbl.blurhash.BlurHash;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import pl.luncher.v3.luncher_core.assets.domainservices.exceptions.CannotEstablishFileTypeException;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.assets.model.AssetUploadStatus;
import pl.luncher.v3.luncher_core.assets.model.MimeContentFileType;


public class AssetFactory {

  public static Asset newFilesystemPersistent(String description, String originalFilename,
      String originalFileContentType, byte[] content) throws IOException {
    MimeContentFileType fileType = MimeContentFileType.fromFilename(originalFilename);

    if (fileType == null) {
      fileType = MimeContentFileType.byMimeType(originalFileContentType);
    }

    if (fileType == null) {
      throw new CannotEstablishFileTypeException();
    }

    if (content == null) {
      throw new IllegalArgumentException("Content cannot be null");
    }

    String blurHash = BlurHash.encode(ImageIO.read(new ByteArrayInputStream(content)));

    return Asset.builder().description(description).uploadStatus(AssetUploadStatus.AWAITING)
        .mimeType(fileType)
        .originalFilename(originalFilename)
        .content(content)
        .blurHash(blurHash)
        .build();
  }
}
