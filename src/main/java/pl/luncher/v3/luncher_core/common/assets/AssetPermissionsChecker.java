package pl.luncher.v3.luncher_core.common.assets;

import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.permissions.PermissionChecker;

public interface AssetPermissionsChecker {

  AssetPermissionsChecker byUser(User user);

  PermissionChecker delete();

  PermissionChecker edit();
}
