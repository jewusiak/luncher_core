package pl.luncher.v3.luncher_core.admin.model.responses;

import lombok.Value;
import pl.luncher.v3.luncher_core.common.domain.infra.AppRole;

import java.util.UUID;

@Value
public class AdminBasicUserDataResponse {
    UUID uuid;
    String email;
    String firstName;
    String surname;
    AppRole role;
    Boolean enabled;
}
