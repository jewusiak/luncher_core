package pl.luncher.v3.luncher_core.common.model.responses;

import lombok.Data;
import pl.luncher.v3.luncher_core.common.domain.infra.AppRole;


@Data
public class UserProfileResponse {
    private String email;
    private AppRole role;
    private String firstName;
    private String surname;
}
