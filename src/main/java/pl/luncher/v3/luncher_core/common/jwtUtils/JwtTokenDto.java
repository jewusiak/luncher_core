package pl.luncher.v3.luncher_core.common.jwtUtils;

import java.util.Date;
import lombok.Value;

@Value
public class JwtTokenDto {

  String token;
  Date expiryDate;
}
