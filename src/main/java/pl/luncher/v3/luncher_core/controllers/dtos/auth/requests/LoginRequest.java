package pl.luncher.v3.luncher_core.controllers.dtos.auth.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

  @NotBlank
  @Email
  private String email;
  @NotBlank
  private String password;
}
