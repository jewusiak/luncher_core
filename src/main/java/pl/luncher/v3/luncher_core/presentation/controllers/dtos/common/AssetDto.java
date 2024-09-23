package pl.luncher.v3.luncher_core.presentation.controllers.dtos.common;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.place.model.Asset}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetDto implements Serializable {

  private UUID uuid;
  private String name;
  private String description;
  private OffsetDateTime dateCreated;
  private String publicUrl;
}
