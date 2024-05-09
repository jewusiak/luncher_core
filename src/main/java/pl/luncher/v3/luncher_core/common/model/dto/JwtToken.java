package pl.luncher.v3.luncher_core.common.model.dto;

import java.util.Date;
import lombok.Value;

@Value
public class JwtToken {

  String token;
  Date expiryDate;
}
