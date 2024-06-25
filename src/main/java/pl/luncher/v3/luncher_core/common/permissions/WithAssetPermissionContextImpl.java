package pl.luncher.v3.luncher_core.common.permissions;

import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class WithAssetPermissionContextImpl implements WithAssetPermissionContext {

  private final UUID assetId;

  @Override
  public PermissionChecker edit() {
    return () -> true;
  }

  @Override
  public PermissionChecker delete() {
    return () -> true;
  }
}
