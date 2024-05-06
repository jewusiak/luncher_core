package pl.luncher.v3.luncher_core.admin.model.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import pl.luncher.v3.luncher_core.common.domain.infra.AppRole;

@Value
public class AdminCreateUserRequest {
    @Email
    @NotBlank
    String email;

    @NotBlank
    String firstName;

    @NotBlank
    String surname;

    @NotBlank //TODO: password security validation
    String password;

    @NotNull
    AppRole role;

    Boolean enabled = true;
}
