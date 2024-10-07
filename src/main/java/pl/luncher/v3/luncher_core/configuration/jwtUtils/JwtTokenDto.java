package pl.luncher.v3.luncher_core.configuration.jwtUtils;

import java.util.Date;
import lombok.Value;

@Value
public class JwtTokenDto {

  String token;
  Date expiryDate;
}
