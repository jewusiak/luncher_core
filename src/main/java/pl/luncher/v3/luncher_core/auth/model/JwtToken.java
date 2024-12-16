package pl.luncher.v3.luncher_core.auth.model;

import java.util.Date;
import lombok.Value;

@Value
public class JwtToken {

  String token;
  Date expiryDate;
}
