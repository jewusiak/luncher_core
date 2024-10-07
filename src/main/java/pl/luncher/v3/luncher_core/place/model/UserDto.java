package pl.luncher.v3.luncher_core.place.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;
import pl.luncher.v3.luncher_core.user.persistence.model.UserDb;

/**
 * DTO for {@link UserDb}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  private UUID uuid;
  private String email;
  private AppRole role;
}
