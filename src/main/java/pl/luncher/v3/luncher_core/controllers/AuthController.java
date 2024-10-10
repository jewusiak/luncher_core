package pl.luncher.v3.luncher_core.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.configuration.jwtUtils.JwtService;
import pl.luncher.v3.luncher_core.user.model.AppRole;
import pl.luncher.v3.luncher_core.controllers.dtos.auth.requests.LoginRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.auth.responses.SuccessfulLoginResponse;
import pl.luncher.v3.luncher_core.controllers.dtos.user.mappers.UserDtoMapper;
import pl.luncher.v3.luncher_core.controllers.dtos.user.requests.NewPasswordRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.user.requests.UserRegistrationRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.user.responses.CreatePasswordResetIntentResponse;
import pl.luncher.v3.luncher_core.user.domainservices.ForgottenPasswordIntentFactory;
import pl.luncher.v3.luncher_core.user.domainservices.ForgottenPasswordIntentPersistenceService;
import pl.luncher.v3.luncher_core.user.domainservices.UserPersistenceService;
import pl.luncher.v3.luncher_core.user.model.User;

@Tag(name = "authentication", description = "Authentication")
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserPersistenceService userPersistenceService;
  private final ForgottenPasswordIntentFactory forgottenPasswordIntentFactory;
  private final ForgottenPasswordIntentPersistenceService forgottenPasswordIntentPersistenceService;
  private final UserDtoMapper userDtoMapper;
  private final PasswordEncoder passwordEncoder;

  @Value("${pl.luncher.security.cookie_domain}")
  private String cookieDomain;

  @Operation(summary = "Login to the system")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully logged in", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessfulLoginResponse.class))}),
      @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)})
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
              .tokenLifetime(accessToken.getExpiryDate().getTime() / 1000L).build(), HttpStatus.OK);
    } catch (Exception e) {
      log.info("Caught exception {} at login.", e.toString());
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }

  @Operation(summary = "Logout from the system", description = "Tries to remove JWT token from cookies. Client should remove JWT token from local storage on their own.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successfully logged out", content = @Content),})
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
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid UserRegistrationRequest request) {
    var passwordHash = passwordEncoder.encode(request.getPassword());
    var user = userDtoMapper.toDomain(request, passwordHash);
    user.setEnabled(true);
    user.setRole(AppRole.USER);

    user.validate();
    userPersistenceService.save(user);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Create Password Reset intent")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Password reset intent created", content = @Content(schema = @Schema(implementation = CreatePasswordResetIntentResponse.class))),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  @PostMapping("/requestreset/{email}")
  public ResponseEntity<?> createPasswordResetIntent(@PathVariable @Email String email) {
    User user = userPersistenceService.getByEmail(email);
    var passwordIntent = forgottenPasswordIntentFactory.of(user);

    var result = forgottenPasswordIntentPersistenceService.save(passwordIntent);

    var response = new CreatePasswordResetIntentResponse(result.getResetUrl(),
        result.getValidityDate().toString());

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Reset password")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Password reset", content = @Content()),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  @PutMapping("/resetpassword/{uuid}")
  public ResponseEntity<?> resetPassword(@PathVariable UUID uuid,
      @RequestBody @Valid NewPasswordRequest request) {
    var passwordIntent = forgottenPasswordIntentPersistenceService.findById(uuid);
    passwordIntent.throwIfNotValid();

    var user = userPersistenceService.getById(passwordIntent.getUserId());
    user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
    userPersistenceService.save(user);

    passwordIntent.invalidate();

    forgottenPasswordIntentPersistenceService.save(passwordIntent);

    return ResponseEntity.noContent().build();

  }
}