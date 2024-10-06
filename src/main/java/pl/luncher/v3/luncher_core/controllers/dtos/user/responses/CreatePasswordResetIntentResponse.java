package pl.luncher.v3.luncher_core.controllers.dtos.user.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePasswordResetIntentResponse {

  private String resetUrl;
  private String validityDate;
}
