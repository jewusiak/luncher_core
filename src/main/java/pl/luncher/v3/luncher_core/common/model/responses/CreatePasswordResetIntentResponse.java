package pl.luncher.v3.luncher_core.common.model.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePasswordResetIntentResponse {

  private String resetUri;
  private String validityDate;
}
