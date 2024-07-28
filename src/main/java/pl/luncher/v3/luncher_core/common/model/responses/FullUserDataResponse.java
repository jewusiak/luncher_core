package pl.luncher.v3.luncher_core.common.model.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;

import java.util.UUID;

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
