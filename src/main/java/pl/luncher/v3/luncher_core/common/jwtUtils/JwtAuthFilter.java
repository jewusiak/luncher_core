package pl.luncher.v3.luncher_core.common.jwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String header = request.getHeader("Authorization");
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      Optional<Cookie> cookie = Arrays.stream(cookies)
          .filter(c -> c.getName().equals("Authorization")).findFirst();
      if (cookie.isPresent()) {
        header = cookie.get().getValue();
      }
    }
    if (header != null) {
      String jwt = header.startsWith("Bearer ") ? header.substring(7) : header;
      var authenticationTokenOptional = jwtService.extractTokenData(jwt);
      authenticationTokenOptional.ifPresent(SecurityContextHolder.getContext()::setAuthentication);
    }

    filterChain.doFilter(request, response);
  }
}
