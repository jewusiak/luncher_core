package pl.luncher.v3.luncher_core.assets.domainservices;

import java.util.Optional;
import java.util.UUID;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.common.permissions.PermissionChecker;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.user.model.User;

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
    return () -> Optional.ofNullable(asset.getPlace()).map(Place::getOwner).map(User::getUuid)
        .map(uuid -> uuid.equals(requestingUserUuid)).orElse(true);

  }

  public PermissionChecker delete() {
    return edit();
  }
}
