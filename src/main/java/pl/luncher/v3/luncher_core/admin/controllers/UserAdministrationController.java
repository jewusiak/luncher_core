package pl.luncher.v3.luncher_core.admin.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.admin.model.mappers.AdminUserMapper;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminCreateUserRequest;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminUpdateUserRequest;
import pl.luncher.v3.luncher_core.admin.model.responses.AdminBasicUserDataResponse;
import pl.luncher.v3.luncher_core.admin.model.responses.AdminFullUserDataResponse;
import pl.luncher.v3.luncher_core.admin.model.responses.AvailableRolesResponse;
import pl.luncher.v3.luncher_core.common.domain.infra.AppRole;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.services.UserService;

@Tag(name = "user-administration", description = "User administration")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@PreAuthorize(AppRole.hasRole.SYS_ADMIN)
public class UserAdministrationController {

  private final UserService userService;
  private final AdminUserMapper adminUserMapper;

  @Operation(summary = "Get all users")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved users", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedAdminUserResponse.class))),
  })
  @GetMapping("")
  public ResponseEntity<?> getUsersPaged(@RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "0") int page) throws InterruptedException {
    Thread.sleep(500);
    return ResponseEntity.ok(
        userService.getAllUsersPaged(PageRequest.of(page, size)).map(adminUserMapper::mapToBasic));
  }

  @Operation(summary = "Get user by UUID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminFullUserDataResponse.class))),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  @GetMapping("/{uuid}")
  public ResponseEntity<?> getUserByUuid(@PathVariable UUID uuid) {
    User userByUuid = userService.getUserByUuid(uuid);
    if (userByUuid == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(adminUserMapper.mapToFull(userByUuid));
  }

  @Operation(summary = "Create user by admin")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully created user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminFullUserDataResponse.class))),
  })
  @PostMapping("")
  public ResponseEntity<?> adminCreateUser(@RequestBody @Valid AdminCreateUserRequest request,
      User user) {
    var userEntity = adminUserMapper.map(request);

    userService.checkIfUserCanCreateOtherUser(userEntity, user);

    return ResponseEntity.ok(adminUserMapper.mapToFull(userService.createUser(userEntity)));
  }

  @Operation(summary = "Update user by admin")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully updated user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminFullUserDataResponse.class))),
  })
  @PatchMapping("{userId}")
  public ResponseEntity<?> adminUpdateUser(@RequestBody @Valid AdminUpdateUserRequest request,
      @PathVariable String userId, User user) {
    var userEntity = userService.mapToUpdateUserByAdmin(request, userId);

    userService.checkIfUserCanUpdateOtherUser(userEntity, user);

    return ResponseEntity.ok(
        adminUserMapper.mapToFull(userService.updateUser(userEntity, UUID.fromString(userId))));
  }

  @Operation(summary = "Full text user search", description = "Empty query will return all users")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved users", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedAdminUserResponse.class))),
  })
  @GetMapping("/search")
  public ResponseEntity<Page<AdminBasicUserDataResponse>> adminSearchUsers(
      @RequestParam(required = false) String query,
      @RequestParam(defaultValue = "20", required = false) int size,
      @RequestParam(defaultValue = "0", required = false) int page) {
    return ResponseEntity.ok(userService.findByStringQueryPaged(query, PageRequest.of(page, size))
        .map(adminUserMapper::mapToBasic));
  }

  @Operation(summary = "Get available roles")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved roles", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvailableRolesResponse.class))),
  })
  @GetMapping("/available_roles")
  public ResponseEntity<?> getAvailableRoles() {
    return ResponseEntity.ok(
        new AvailableRolesResponse(Arrays.stream(AppRole.values()).map(AppRole::name).toList()));
  }

  // swagger schema class
  interface PagedAdminUserResponse extends Page<AdminBasicUserDataResponse> {

  }
}
