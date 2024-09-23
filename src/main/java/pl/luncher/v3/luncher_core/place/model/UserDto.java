package pl.luncher.v3.luncher_core.place.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.common.persistence.models.UserDb}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  private UUID uuid;
  private String email;
  private AppRole role;
}
