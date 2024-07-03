package pl.luncher.v3.luncher_core.common.assets;

import lombok.RequiredArgsConstructor;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.permissions.PermissionChecker;

@RequiredArgsConstructor
class AssetPermissionsCheckerImpl implements AssetPermissionsChecker {

  private final CommonAssetImpl asset;
  private User user;

  @Override
  public AssetPermissionsChecker byUser(User user) {
    this.user = user;
    return this;
  }

  @Override
  public PermissionChecker delete() {
    return () -> asset.getCommonAssetDb().getRefToPlaceImages().getOwner().equals(user);
  }

  @Override
  public PermissionChecker edit() {
    return () -> asset.getCommonAssetDb().getRefToPlaceImages().getOwner().equals(user);
  }
}
