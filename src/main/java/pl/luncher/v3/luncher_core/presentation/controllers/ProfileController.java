package pl.luncher.v3.luncher_core.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.responses.UserProfileResponse;
import pl.luncher.v3.luncher_core.common.domain.users.User;

@Tag(name = "profile", description = "User profiles")
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

  @Operation(summary = "Who am I?")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns your profile", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponse.class))),
      @ApiResponse(responseCode = "401", description = "Not authorized!", content = @Content),
      @ApiResponse(responseCode = "406", description = "User can't be extracted from context!", content = @Content)
  })
  @GetMapping
  public ResponseEntity<?> getProfile(User requestingUser) {
    return ResponseEntity.ok(requestingUser.castToProfileResponse());
  }
}