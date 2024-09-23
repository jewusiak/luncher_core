package pl.luncher.v3.luncher_core.presentation.controllers.dtos.responses;

import java.util.List;
import lombok.Value;

@Value
public class AvailableRolesResponse {

  List<String> roles;

}
