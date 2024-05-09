package pl.luncher.v3.luncher_core.admin.model.responses;

import java.util.List;
import lombok.Value;

@Value
public class AvailableRolesResponse {

  List<String> roles;
}
