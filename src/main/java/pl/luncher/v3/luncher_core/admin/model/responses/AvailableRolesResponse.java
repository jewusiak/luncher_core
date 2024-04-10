package pl.luncher.v3.luncher_core.admin.model.responses;

import lombok.Value;

import java.util.List;

@Value
public class AvailableRolesResponse {
    List<String> roles;
}
