package pl.luncher.v3.luncher_core.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.controllers.dtos.user.mappers.UserDtoMapper;
import pl.luncher.v3.luncher_core.controllers.dtos.user.requests.UserCreateRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.user.requests.UserUpdateRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.user.responses.AvailableRolesResponse;
import pl.luncher.v3.luncher_core.controllers.dtos.user.responses.UserBasicResponse;
import pl.luncher.v3.luncher_core.controllers.errorhandling.model.ErrorResponse;
import pl.luncher.v3.luncher_core.user.domainservices.UserPersistenceService;
import pl.luncher.v3.luncher_core.user.domainservices.UserSearchService;
import pl.luncher.v3.luncher_core.user.model.AppRole;
import pl.luncher.v3.luncher_core.user.model.AppRole.hasRole;
import pl.luncher.v3.luncher_core.user.model.User;

@Tag(name = "users", description = "User administration")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@PreAuthorize(hasRole.SYS_MOD)
public class UsersController {

  private final UserSearchService userSearchService;
  private final UserDtoMapper userDtoMapper;
  private final UserPersistenceService userPersistenceService;
  private final PasswordEncoder passwordEncoder;

  @Operation(summary = "Get all users")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved users", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListOfBasicUserDataResponse.class))),
  })
  @GetMapping("")
  public ResponseEntity<List<UserBasicResponse>> getUsersPaged(
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "0") int page) {
    var usersPage = userSearchService.search(null, page, size);

    return ResponseEntity.ok(
        usersPage.stream().map(userDtoMapper::toUserBasicResponse).collect(Collectors.toList()));
  }

  @Operation(summary = "Get user by UUID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserBasicResponse.class))),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  @GetMapping("/{uuid}")
  public ResponseEntity<UserBasicResponse> getUserByUuid(@PathVariable UUID uuid) {
    User user = userPersistenceService.getById(uuid);

    return ResponseEntity.ok(userDtoMapper.toUserBasicResponse(user));
  }

  @Operation(summary = "Create user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully created user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserBasicResponse.class))),
  })
  @PostMapping("")
  public ResponseEntity<UserBasicResponse> createUser(@RequestBody @Valid UserCreateRequest request,
      @Parameter(hidden = true) User requestingUser) {
    var user = userDtoMapper.toDomain(request, passwordEncoder.encode(request.getPassword()));

    user.permissions().byUser(requestingUser).createThisUser().throwIfNotPermitted();

    user.validate();
    var saved = userPersistenceService.save(user);

    return ResponseEntity.ok(userDtoMapper.toUserBasicResponse(saved));
  }

  @Operation(summary = "Update user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully updated user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserBasicResponse.class))),
  })
  @PutMapping("{userId}")
  public ResponseEntity<UserBasicResponse> updateUser(@RequestBody UserUpdateRequest request,
      @PathVariable UUID userId, @Parameter(hidden = true) User requestingUser) {
    var user = userPersistenceService.getById(userId);
    userDtoMapper.updateDomain(user, request,
        request.getPassword() == null ? null : passwordEncoder.encode(request.getPassword()));

    user.permissions().byUser(requestingUser).edit().throwIfNotPermitted();

    user.validate();
    var saved = userPersistenceService.save(user);

    return ResponseEntity.ok(userDtoMapper.toUserBasicResponse(saved));
  }

  @Operation(summary = "Delete user")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Deleted successfully", content = @Content),
      @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @DeleteMapping("{uuid}")
  public ResponseEntity<Void> deleteUser(@NotNull @PathVariable UUID uuid,
      @Parameter(hidden = true) User requestingUser) {
    var user = userPersistenceService.getById(uuid);

    user.permissions().byUser(requestingUser).delete().throwIfNotPermitted();

    userPersistenceService.deleteById(uuid);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Full text user search", description = "Empty query will return all users")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved users", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListOfBasicUserDataResponse.class))),
  })
  @GetMapping("/search")
  public ResponseEntity<List<UserBasicResponse>> adminSearchUsers(
      @RequestParam(required = false) String query,
      @RequestParam(defaultValue = "20", required = false) int size,
      @RequestParam(defaultValue = "0", required = false) int page) {
    var userList = userSearchService.search(query, page, size);

    return ResponseEntity.ok(
        userList.stream().map(userDtoMapper::toUserBasicResponse).collect(Collectors.toList()));
  }

  @Operation(summary = "Get available roles")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved roles", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvailableRolesResponse.class))),
  })
  @GetMapping("/available_roles")
  public ResponseEntity<AvailableRolesResponse> getAvailableRoles() {
    var roles = Arrays.stream(AppRole.values()).map(Enum::name).toList();
    return ResponseEntity.ok(new AvailableRolesResponse(roles));
  }

  // swagger schema class
  interface ListOfBasicUserDataResponse extends List<UserBasicResponse> {

  }
}
