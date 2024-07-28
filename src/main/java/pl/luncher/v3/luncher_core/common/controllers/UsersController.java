package pl.luncher.v3.luncher_core.common.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.common.controllers.errorhandling.model.ErrorResponse;
import pl.luncher.v3.luncher_core.common.domain.users.User;
import pl.luncher.v3.luncher_core.common.domain.users.UserFactory;
import pl.luncher.v3.luncher_core.common.model.requests.UserCreateRequest;
import pl.luncher.v3.luncher_core.common.model.requests.UserUpdateRequest;
import pl.luncher.v3.luncher_core.common.model.responses.AvailableRolesResponse;
import pl.luncher.v3.luncher_core.common.model.responses.BasicUserDataResponse;
import pl.luncher.v3.luncher_core.common.model.responses.FullUserDataResponse;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole.hasRole;

@Tag(name = "users", description = "User administration")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@PreAuthorize(hasRole.SYS_MOD)
public class UsersController {

  private final UserFactory userFactory;

  @Operation(summary = "Get all users")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved users", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedBasicUserDataResponse.class))),
  })
  @GetMapping("")
  public ResponseEntity<Page<BasicUserDataResponse>> getUsersPaged(@RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "0") int page) {
    var usersPage = userFactory.findByStringQueryPaged(null, PageRequest.of(page, size));

    return ResponseEntity.ok(usersPage.map(User::castToBasicDataResponse));
  }

  @Operation(summary = "Get user by UUID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FullUserDataResponse.class))),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  @GetMapping("/{uuid}")
  public ResponseEntity<?> getUserByUuid(@PathVariable UUID uuid) {
    User user = userFactory.pullFromRepo(uuid);

    return ResponseEntity.ok(user.castToFullDataResponse());
  }

  @Operation(summary = "Create user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully created user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FullUserDataResponse.class))),
  })
  @PostMapping("")
  public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateRequest request, User requestingUser) {
    var user = userFactory.of(request);

    user.permissions().byUser(requestingUser).createThisUser().throwIfNotPermitted();

    user.save();

    return ResponseEntity.ok(user.castToFullDataResponse());
  }

  @Operation(summary = "Update user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully updated user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FullUserDataResponse.class))),
  })
  @PutMapping("{userId}")
  public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest request,
      @PathVariable UUID userId, User requestingUser) {
    var user = userFactory.pullFromRepo(userId);

    user.permissions().byUser(requestingUser).update().throwIfNotPermitted();

    user.updateWith(request);
    user.save();

    return ResponseEntity.ok(user.castToFullDataResponse());
  }
  
  @Operation(summary = "Delete user")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Deleted successfully", content = @Content),
      @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @DeleteMapping("{uuid}")
  public ResponseEntity<?> deleteUser(@NotNull @PathVariable UUID uuid, User requestingUser) {
    var user = userFactory.pullFromRepo(uuid);
    
    user.permissions().byUser(requestingUser).delete().throwIfNotPermitted();
    
    user.delete();
    
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Full text user search", description = "Empty query will return all users")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved users", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedBasicUserDataResponse.class))),
  })
  @GetMapping("/search")
  public ResponseEntity<Page<BasicUserDataResponse>> adminSearchUsers(
      @RequestParam(required = false) String query,
      @RequestParam(defaultValue = "20", required = false) int size,
      @RequestParam(defaultValue = "0", required = false) int page) {
    var usersPage = userFactory.findByStringQueryPaged(query, PageRequest.of(page, size));
    return ResponseEntity.ok(usersPage.map(User::castToBasicDataResponse));
  }

  @Operation(summary = "Get available roles")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved roles", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvailableRolesResponse.class))),
  })
  @GetMapping("/available_roles")
  public ResponseEntity<?> getAvailableRoles() {
    var roles = Arrays.stream(AppRole.values()).map(Enum::name).toList();
    return ResponseEntity.ok(new AvailableRolesResponse(roles));
  }

  // swagger schema class
  interface PagedBasicUserDataResponse extends Page<BasicUserDataResponse> {

  }
}
