package pl.luncher.v3.luncher_core.common.jwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.common.domain.users.User;
import pl.luncher.v3.luncher_core.common.model.dto.JwtToken;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;

@Component
@Slf4j
public class JwtService {

  @Value("${pl.luncher.jwt.signing_key}")
  private String signingKey;

  @Value("${pl.luncher.jwt.access_token_lifetime_minutes:10}")
  private int accessTokenLifetimeMinutes;


  /**
   * If token is valid, returns user UUID and authorities from JWT token
   *
   * @param jwt token
   * @return user's data
   */
  public Optional<UsernamePasswordAuthenticationToken> extractTokenData(String jwt) {
    try {
      Claims claims = validateAndExtractAllClaims(jwt);
      var uuid = extractUuid(claims);
      var authorities = extractRole(claims);

      var springAuthToken = UsernamePasswordAuthenticationToken.authenticated(uuid, null,
          authorities);

      return Optional.of(springAuthToken);
    } catch (Exception e) {
      log.info("Caught exception on JWT extraction: {}.", e.getMessage());
      return Optional.empty();
    }
  }

  public JwtToken generateJwtTokenForUser(User user) {
    Date expiration = new Date(System.currentTimeMillis() + accessTokenLifetimeMinutes * 60000L);
    String token = Jwts.builder().setClaims(new HashMap<>() {{
          put("role", user.getRole());
        }}).setSubject(user.getUuid().toString())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(expiration)
        .signWith(SignatureAlgorithm.HS256, signingKey).compact();
    return new JwtToken(token, expiration);
  }


  private Claims validateAndExtractAllClaims(String token) {
    return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();
  }

  private UUID extractUuid(Claims claims) {
    String sub = claims.getSubject();
    if (sub == null) {
      return null;
    }
    try {
      return UUID.fromString(sub);
    } catch (Exception e) {
      return null;
    }
  }

  private List<SimpleGrantedAuthority> extractRole(Claims claims) {
    String roleClaim = claims.get("role", String.class);
    try {
      AppRole appRole = AppRole.valueOf(roleClaim);
      return List.of(appRole.getAuthorityObj());
    } catch (Exception ignored) {
      return new ArrayList<>();
    }
  }
}
