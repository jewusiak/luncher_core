package pl.luncher.v3.luncher_core.application.controllers.dtos.auth.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

  @NotBlank
  @Email
  private String email;
  @NotBlank
  private String password;
}
