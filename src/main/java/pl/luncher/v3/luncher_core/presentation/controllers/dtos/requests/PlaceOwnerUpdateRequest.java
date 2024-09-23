package pl.luncher.v3.luncher_core.presentation.controllers.dtos.requests;

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
public class PlaceOwnerUpdateRequest {

  @NotBlank
  @Email
  private String email;
}
