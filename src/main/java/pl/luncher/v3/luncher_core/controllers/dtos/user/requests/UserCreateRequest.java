package pl.luncher.v3.luncher_core.controllers.dtos.user.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import pl.luncher.v3.luncher_core.user.model.AppRole;

@Data
public class UserCreateRequest {

  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String firstName;

  @NotBlank
  private String surname;

  @NotNull
  @Size(min = 4)
  private String password;

  @NotNull
  private AppRole role;

  private Boolean enabled = true;
}
