package pl.luncher.v3.luncher_core.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.application.controllers.dtos.auth.requests.LoginRequest;
import pl.luncher.v3.luncher_core.application.controllers.dtos.auth.responses.SuccessfulLoginResponse;
import pl.luncher.v3.luncher_core.application.controllers.dtos.user.mappers.UserDtoMapper;
import pl.luncher.v3.luncher_core.application.controllers.dtos.user.requests.NewPasswordRequest;
import pl.luncher.v3.luncher_core.application.controllers.dtos.user.requests.UserRegistrationRequest;
import pl.luncher.v3.luncher_core.application.controllers.dtos.user.responses.CreatePasswordResetIntentResponse;
import pl.luncher.v3.luncher_core.auth.model.JwtToken;
import pl.luncher.v3.luncher_core.auth.services.AuthorizationCookieService;
import pl.luncher.v3.luncher_core.auth.services.JwtService;
import pl.luncher.v3.luncher_core.user.domainservices.interfaces.ForgottenPasswordService;
import pl.luncher.v3.luncher_core.user.domainservices.interfaces.UserRegistrationService;
import pl.luncher.v3.luncher_core.user.model.User;

@Tag(name = "authentication", description = "Authentication")
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserDtoMapper userDtoMapper;
  private final AuthorizationCookieService authorizationCookieService;
  private final UserRegistrationService userRegistrationService;
  private final ForgottenPasswordService forgottenPasswordService;

  private static SuccessfulLoginResponse mapToLoginResponse(JwtToken accessToken) {
    return SuccessfulLoginResponse.builder().accessToken(accessToken.getToken())
        .tokenLifetime(accessToken.getExpiryDate().getTime() / 1000L).build();
  }

  @Operation(summary = "Login to the system")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully logged in", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessfulLoginResponse.class))}),
      @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)})
  @PostMapping("/login")
  public ResponseEntity<SuccessfulLoginResponse> login(
      @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true) @Valid LoginRequest request,
      HttpServletResponse response) {
    try {
      var authObject = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

      var accessToken = jwtService.generateJwtTokenForUser((User) authObject.getPrincipal());

      response.addCookie(authorizationCookieService.generateCookie(accessToken));

      return new ResponseEntity<>(
          mapToLoginResponse(accessToken), HttpStatus.OK);
    } catch (Exception e) {
      log.info("Caught exception {} at login.", e.toString());
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }

  @Operation(summary = "Logout from the system", description = "Tries to remove JWT token from cookies. Client should remove JWT token from local storage on their own.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successfully logged out", content = @Content),})
  @DeleteMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletResponse response) {
    response.addCookie(authorizationCookieService.getLogoutCookie());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Operation(summary = "End user registration")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Successful registration", content = @Content()),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody @Valid UserRegistrationRequest request) {
    var user = userDtoMapper.toDomain(request);

    userRegistrationService.registerNewUser(user, request.getPassword());

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Create Password Reset intent")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Password reset intent created", content = @Content(schema = @Schema(implementation = CreatePasswordResetIntentResponse.class))),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  @PostMapping("/requestreset/{email}")
  public ResponseEntity<CreatePasswordResetIntentResponse> createPasswordResetIntent(
      @PathVariable @Email @NotNull String email) {
    var result = forgottenPasswordService.createPasswordResetIntent(email);

    var response = new CreatePasswordResetIntentResponse(result.getResetUrl(),
        result.getValidityDate().toString());

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Reset password")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Password reset", content = @Content()),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  @PutMapping("/resetpassword/{uuid}")
  public ResponseEntity<Void> resetPassword(@PathVariable UUID uuid,
      @RequestBody @Valid NewPasswordRequest request) {

    forgottenPasswordService.resetWithToken(uuid, request.getNewPassword());

    return ResponseEntity.noContent().build();

  }
}
