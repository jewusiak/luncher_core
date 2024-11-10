package pl.luncher.v3.luncher_core.controllers.dtos.common;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetBasicResponse implements Serializable {

  private UUID id;
  private String description;
  private String accessUrl;
  private String mimeType;
  private String uploadStatus;
}
