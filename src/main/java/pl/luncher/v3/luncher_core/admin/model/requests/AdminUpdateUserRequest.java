package pl.luncher.v3.luncher_core.admin.model.requests;

import jakarta.validation.constraints.Email;
import lombok.Value;
import pl.luncher.v3.luncher_core.common.domain.infra.AppRole;

@Value
public class AdminUpdateUserRequest {
    @Email
    String email;

    String firstName;

    String surname;

    String password;

    AppRole role;
}
