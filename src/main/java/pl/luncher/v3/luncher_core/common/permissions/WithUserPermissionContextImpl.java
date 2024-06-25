package pl.luncher.v3.luncher_core.common.permissions;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WithUserPermissionContextImpl implements WithUserPermissionContext {

  @Getter(AccessLevel.PROTECTED)
  private final UUID userId;

  @Override
  public WithPlacePermissionContext withPlace(UUID placeId) {
    return new WithPlacePermissionContextImpl(placeId);
  }

  @Override
  public WithAssetPermissionContext withAsset(UUID assetId) {
    return new WithAssetPermissionContextImpl(assetId);
  }
}
