package pl.luncher.v3.luncher_core.application.controllers.dtos.user.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {

  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String firstName;

  @NotBlank
  private String surname;

  @NotBlank
  private String password;

}
