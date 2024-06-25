package pl.luncher.v3.luncher_core.common.permissions;

import java.util.UUID;

public interface WithUserPermissionContext {

  WithPlacePermissionContext withPlace(UUID placeId);

  WithAssetPermissionContext withAsset(UUID assetId);
}
