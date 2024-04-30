package pl.luncher.v3.luncher_core.common.model.dto;

import lombok.Value;

import java.util.Date;

@Value
public class JwtToken {
    String token;
    Date expiryDate;
}
