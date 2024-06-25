package pl.luncher.v3.luncher_core.common.controllers;

import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.model.requests.CreateAssetRequest;
import pl.luncher.v3.luncher_core.common.model.requests.CreateAssetRequest.AssetParentType;
import pl.luncher.v3.luncher_core.common.permissions.PermissionChecker;
import pl.luncher.v3.luncher_core.common.permissions.PermissionCheckerFactory;
import pl.luncher.v3.luncher_core.common.permissions.WithUserPermissionContext;

@RestController("/assets")
@RequiredArgsConstructor
public class AssetController {

  private final PermissionCheckerFactory permissionCheckerFactory;

  @PostMapping
  @Operation(summary = "Create asset")
  public ResponseEntity<?> create(@RequestBody CreateAssetRequest request, User user) {

    WithUserPermissionContext withUserPermissionContext = permissionCheckerFactory.withUser(
        user.getUuid());

    PermissionChecker permissionChecker;
    if (request.getParentType() == AssetParentType.PLACE) {
      permissionChecker = withUserPermissionContext.withPlace(
          UUID.fromString(request.getParentRef())).edit();
    } else {
      throw new UnsupportedOperationException("Unsupported asset parent type");
    }
    permissionChecker.checkPermission();

    // this should verify if user has permissions to create and link the asset to a place
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @DeleteMapping("/{uuid}")
  @Operation(summary = "Unlink and delete asset")
  public ResponseEntity<?> delete(@PathVariable UUID uuid) {
    // this should verify if user has permissions to delete the asset
    throw new UnsupportedOperationException("Not yet implemented");
  }

}
