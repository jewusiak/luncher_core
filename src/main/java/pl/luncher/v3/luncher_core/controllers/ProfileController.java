package pl.luncher.v3.luncher_core.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import pl.luncher.v3.luncher_core.controllers.dtos.user.mappers.UserDtoMapper;
import pl.luncher.v3.luncher_core.controllers.dtos.user.responses.UserProfileResponse;
import pl.luncher.v3.luncher_core.user.model.User;

@Tag(name = "profile", description = "User profiles")
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

  private final UserDtoMapper userDtoMapper;

  @Operation(summary = "Who am I?")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returns your profile", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponse.class))),
      @ApiResponse(responseCode = "401", description = "Not authorized!", content = @Content),
      @ApiResponse(responseCode = "406", description = "User can't be extracted from context!", content = @Content)
  })
  @GetMapping
  public ResponseEntity<UserProfileResponse> getProfile(@Parameter(hidden = true) User requestingUser) {
    return ResponseEntity.ok(userDtoMapper.toUserProfileResponse(requestingUser));
  }
}
