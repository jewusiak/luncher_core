package pl.luncher.v3.luncher_core.admin.model.responses;

import java.util.UUID;
import lombok.Value;
import pl.luncher.v3.luncher_core.common.domain.infra.AppRole;

@Value
public class AdminBasicUserDataResponse {

  UUID uuid;
  String email;
  String firstName;
  String surname;
  AppRole role;
  Boolean enabled;
}
