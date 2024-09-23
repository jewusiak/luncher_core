package pl.luncher.v3.luncher_core.presentation.controllers.dtos.responses;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullUserDataResponse {

  private UUID uuid;
  private String email;
  private String firstName;
  private String surname;
  private AppRole role;
  private Boolean enabled;
}
