package pl.luncher.v3.luncher_core.auth.services;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.auth.model.JwtToken;

@Service
@RequiredArgsConstructor
public class AuthorizationCookieService {

  @Value("${pl.luncher.security.cookie_domain}")
  private String cookieDomain;

  public Cookie generateCookie(JwtToken accessToken) {
    return new Cookie("Authorization", accessToken.getToken()) {{
      //todo: set up cookie flags
//                setHttpOnly(true);
      setMaxAge(60 * 15);
      setDomain(cookieDomain);
      setPath("/");
      setSecure(true);
      setAttribute("SameSite", "None");
    }};
  }

  public Cookie getLogoutCookie() {
    return new Cookie("Authorization", null) {{
      setMaxAge(1);
      setDomain(cookieDomain);
      setPath("/");
      setSecure(true);
      setAttribute("SameSite", "None");
    }};
  }
}
