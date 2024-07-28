package pl.luncher.v3.luncher_core.common.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.common.domain.users.User;
import pl.luncher.v3.luncher_core.common.domain.users.UserFactory;
import pl.luncher.v3.luncher_core.common.jwtUtils.JwtService;
import pl.luncher.v3.luncher_core.common.model.requests.LoginRequest;
import pl.luncher.v3.luncher_core.common.model.requests.UserRegistrationRequest;
import pl.luncher.v3.luncher_core.common.model.responses.SuccessfulLoginResponse;

@Tag(name = "authentication", description = "Authentication")
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserFactory userFactory;

  @Value("${pl.luncher.security.cookie_domain}")
  private String cookieDomain;

  @Operation(summary = "Login to the system")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully logged in", content = {
          @Content(mediaType = "application/json",
              schema = @Schema(implementation = SuccessfulLoginResponse.class))
      }),
      @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
  })
  @PostMapping("/login")
  public ResponseEntity<?> login(
      @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody LoginRequest request,
      HttpServletResponse response) {
    try {
      var a = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

      var accessToken = jwtService.generateJwtTokenForUser((User) a.getPrincipal());

      response.addCookie(new Cookie("Authorization", accessToken.getToken()) {{
        //setSecure(true);
        //todo: set up cookie flags
//                setSecure(true);
//                setHttpOnly(true);
        setMaxAge(60 * 15);
        setDomain(cookieDomain);
        setPath("/");
        setSecure(true);
        setAttribute("SameSite", "None");
      }});
      return new ResponseEntity<>(
          SuccessfulLoginResponse.builder().accessToken(accessToken.getToken())
              .tokenLifetime(accessToken.getExpiryDate().getTime() / 1000L).build(),
          HttpStatus.OK);
    } catch (Exception e) {
      log.info("Caught exception {} at login.", e.toString());
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }

  @Operation(summary = "Logout from the system",
      description = "Tries to remove JWT token from cookies. Client should remove JWT token from local storage on their own.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successfully logged out", content = @Content),
  })
  @DeleteMapping("/logout")
  public ResponseEntity<?> logout(HttpServletResponse response) {
    response.addCookie(new Cookie("Authorization", null) {{
      setMaxAge(1);
      setDomain(cookieDomain);
      setPath("/");
      setSecure(true);
      setAttribute("SameSite", "None");
    }});
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
  
  @Operation(summary = "End user registration")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Successful registration", content = @Content()),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))      
  })
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid UserRegistrationRequest request) {
    var user = userFactory.of(request);
    
    user.save();
    
    return ResponseEntity.noContent().build();
  }
}
