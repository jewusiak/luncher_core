package pl.luncher.v3.luncher_core.common.domain.place.domain.dtos;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.common.persistence.models.UserDb}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto implements Serializable {

  private UUID uuid;
  private String email;
  private String firstName;
  private String surname;
  private AppRole role;
}