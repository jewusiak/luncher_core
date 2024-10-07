package pl.luncher.v3.luncher_core.controllers.dtos.auth.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

  private String email;
  private String password;
}
