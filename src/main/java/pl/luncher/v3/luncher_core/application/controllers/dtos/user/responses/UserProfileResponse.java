package pl.luncher.v3.luncher_core.application.controllers.dtos.user.responses;

import java.util.UUID;
import lombok.Data;
import pl.luncher.v3.luncher_core.user.model.AppRole;


@Data
public class UserProfileResponse {

  private UUID uuid;
  private String email;
  private AppRole role;
  private String firstName;
  private String surname;
}
