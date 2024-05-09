package pl.luncher.v3.luncher_core.common.model.requests;

import lombok.Data;

@Data
public class LoginRequest {

  private String email;
  private String password;
}
