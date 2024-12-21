package pl.luncher.v3.luncher_core.application.controllers.dtos.user.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.user.model.AppRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

  @Email
  private String email;
  //  @NotBlank
  private String firstName;
  //  @NotBlank
  private String surname;
  @Size(min = 4)
  private String password;
  private AppRole role;
  private Boolean enabled;
}
