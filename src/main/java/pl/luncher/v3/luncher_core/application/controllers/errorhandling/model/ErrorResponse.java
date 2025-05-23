package pl.luncher.v3.luncher_core.application.controllers.errorhandling.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

  private String message;
  private String cause;
  private String messageLocale;
}
