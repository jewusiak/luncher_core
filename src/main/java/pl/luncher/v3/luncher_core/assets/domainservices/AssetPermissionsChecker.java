package pl.luncher.v3.luncher_core.assets.domainservices;

import java.util.UUID;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.common.permissions.PermissionChecker;

public class AssetPermissionsChecker {

  private final Asset asset;
  private UUID requestingUserUuid;

  public AssetPermissionsChecker(Asset asset) {
    this.asset = asset;
  }

  public AssetPermissionsChecker byUser(UUID requestingUserUuid) {
    this.requestingUserUuid = requestingUserUuid;
    return this;
  }

  public PermissionChecker edit() {
    return () -> {
      if (asset.getPlaceOwnerId() == null) {
        return true;
      }
      return asset.getPlaceOwnerId().equals(requestingUserUuid);
    };
  }

  public PermissionChecker delete() {
    return edit();
  }
}
