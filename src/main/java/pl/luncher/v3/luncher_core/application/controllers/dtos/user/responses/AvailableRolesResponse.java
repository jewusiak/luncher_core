package pl.luncher.v3.luncher_core.application.controllers.dtos.user.responses;

import java.util.List;
import lombok.Value;

@Value
public class AvailableRolesResponse {

  List<String> roles;

}
