package pl.luncher.v3.luncher_core.controllers.dtos.user.responses;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.user.model.User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicResponse implements Serializable {

  private UUID uuid;
  private String email;
  private String firstName;
  private String surname;
  private AppRole role;
  private Boolean enabled;
}