package pl.luncher.v3.luncher_core.common.model.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;

@Value
public class UserCreateRequest {

  @Email
  @NotBlank
  String email;

  @NotBlank
  String firstName;

  @NotBlank
  String surname;

  @NotBlank //TODO: password security validation
  String password;

  @NotNull
  AppRole role;

  Boolean enabled = true;
}
