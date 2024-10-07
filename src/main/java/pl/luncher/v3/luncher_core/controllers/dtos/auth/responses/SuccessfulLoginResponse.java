package pl.luncher.v3.luncher_core.controllers.dtos.auth.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuccessfulLoginResponse {

  private String accessToken;
  private Long tokenLifetime;
}
