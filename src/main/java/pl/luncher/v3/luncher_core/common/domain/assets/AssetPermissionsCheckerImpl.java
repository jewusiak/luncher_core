package pl.luncher.v3.luncher_core.common.domain.assets;

import lombok.RequiredArgsConstructor;
import pl.luncher.v3.luncher_core.common.domain.users.User;
import pl.luncher.v3.luncher_core.common.permissions.PermissionChecker;

@RequiredArgsConstructor
class AssetPermissionsCheckerImpl implements AssetPermissionsChecker {

  private final ImageAssetImpl asset;
  private User user;

  @Override
  public AssetPermissionsChecker byUser(User user) {
    this.user = user;
    return this;
  }

  @Override
  public PermissionChecker delete() {
    throw new UnsupportedOperationException("To be implemented!");
//    return () -> asset.getPlace().getOwner().equals(user);
  }

  @Override
  public PermissionChecker edit() {
    throw new UnsupportedOperationException("To be implemented!");
//    return () -> asset.getPlace().getOwner().equals(user);
  }
}
