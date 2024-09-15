package pl.luncher.v3.luncher_core.common.domain.place.model.dtos;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.common.persistence.models.OpeningWindowDb}
 */
@AllArgsConstructor
@Getter
public class OpeningWindowDto implements Serializable {

  private final UUID uuid;
  private final int startTime;
  private final int endTime;
}