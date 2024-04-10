package pl.luncher.v3.luncher_core.common.jwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.services.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    //REMOVE:
    private final UserService userService;

    @Value("${pl.luncher.security.bypassLogin:false}")
    private boolean bypassLogin;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //TODO: Remove
        String superHeader = bypassLogin ? request.getHeader("X-LOGIN-AS") : null;
        if (superHeader != null) {
            Optional<User> userOptional = userService.findUserByEmail(superHeader);
            if (userOptional.isPresent()) {
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userOptional.get().getUuid(), null, userOptional.get().getAuthorities()));
                filterChain.doFilter(request, response);
                return;
            }
        }

        String header = request.getHeader("Authorization");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> cookie = Arrays.stream(cookies).filter(c -> c.getName().equals("Authorization")).findFirst();
            if (cookie.isPresent()) header = cookie.get().getValue();
        }
        if (header != null) {
            String jwt = header.startsWith("Bearer ") ? header.substring(7) : header;
            var authenticationTokenOptional = jwtService.extractTokenData(jwt);
            authenticationTokenOptional.ifPresent(SecurityContextHolder.getContext()::setAuthentication);
        }

        filterChain.doFilter(request, response);
    }
}
