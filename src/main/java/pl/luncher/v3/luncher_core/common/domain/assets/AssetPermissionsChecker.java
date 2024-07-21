package pl.luncher.v3.luncher_core.common.domain.assets;

import pl.luncher.v3.luncher_core.common.domain.users.User;
import pl.luncher.v3.luncher_core.common.permissions.PermissionChecker;

public interface AssetPermissionsChecker {

  AssetPermissionsChecker byUser(User user);

  PermissionChecker delete();

  PermissionChecker edit();
}
