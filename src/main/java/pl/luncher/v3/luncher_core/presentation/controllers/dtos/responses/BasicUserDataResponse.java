package pl.luncher.v3.luncher_core.presentation.controllers.dtos.responses;

import java.util.UUID;
import lombok.Value;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;

@Value
public class BasicUserDataResponse {

  UUID uuid;
  String email;
  String firstName;
  String surname;
  AppRole role;
  Boolean enabled;
}
