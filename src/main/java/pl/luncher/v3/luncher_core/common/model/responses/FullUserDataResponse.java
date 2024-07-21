package pl.luncher.v3.luncher_core.common.model.responses;

import java.util.UUID;
import lombok.Value;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;

@Value
public class FullUserDataResponse {

  UUID uuid;
  String email;
  String firstName;
  String surname;
  AppRole role;
  Boolean enabled;
}
