package pl.luncher.v3.luncher_core.presentation.controllers.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;

@Data
public class UserCreateRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank private String firstName;

    @NotBlank private String surname;

    @NotBlank //TODO: password security validation
    private String password;

    @NotNull private AppRole role;

    private Boolean enabled = true;
}
