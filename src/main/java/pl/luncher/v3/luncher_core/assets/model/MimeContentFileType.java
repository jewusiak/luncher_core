package pl.luncher.v3.luncher_core.assets.model;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MimeContentFileType {
  PNG("png", "image/png", Set.of("png")),
  JPEG("jpeg", "image/jpeg", Set.of("jpeg", "jpg")),
  PDF("pdf", "application/pdf", Set.of("pdf"));

  private final String outputExtension;
  private final String mimeType;
  private final Set<String> qualifyingExtensions;

  public static MimeContentFileType fromExtension(String extension) {
    for (MimeContentFileType mimeContentFileType : MimeContentFileType.values()) {
      if (mimeContentFileType.getQualifyingExtensions().contains(extension)) {
        return mimeContentFileType;
      }
    }
    throw new IllegalArgumentException("Unknown extension: " + extension);
  }

}
