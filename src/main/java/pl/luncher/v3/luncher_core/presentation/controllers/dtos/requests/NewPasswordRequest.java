package pl.luncher.v3.luncher_core.presentation.controllers.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewPasswordRequest {

  @NotBlank
  private String newPassword;
}