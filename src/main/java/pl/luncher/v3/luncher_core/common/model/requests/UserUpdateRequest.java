package pl.luncher.v3.luncher_core.common.model.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

//  @Email
  private String email;
//  @NotBlank
  private String firstName;
//  @NotBlank
  private String surname;
  private String password;
  private AppRole role;
  private Boolean enabled;
}
