package pl.luncher.v3.luncher_core.place.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetDto {

  private UUID uuid;
  private String name;
  private String description;
  private OffsetDateTime dateCreated;
  private String publicUrl;
}
