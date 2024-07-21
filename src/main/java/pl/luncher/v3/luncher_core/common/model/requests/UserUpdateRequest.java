package pl.luncher.v3.luncher_core.common.model.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;

@Value
public class UserUpdateRequest {

  @Email
  String email;
  @NotBlank
  String firstName;
  @NotBlank
  String surname;
  String password;
  AppRole role;
  Boolean enabled;
}
