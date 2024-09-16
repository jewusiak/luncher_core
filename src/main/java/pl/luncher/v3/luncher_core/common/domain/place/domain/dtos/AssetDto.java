package pl.luncher.v3.luncher_core.common.domain.place.domain.dtos;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.common.persistence.models.AssetDb}
 */
@AllArgsConstructor
@Getter
public class AssetDto implements Serializable {

  private final UUID uuid;
  private final String name;
  private final String description;
  private final OffsetDateTime dateCreated;
  private final String publicUrl;
}