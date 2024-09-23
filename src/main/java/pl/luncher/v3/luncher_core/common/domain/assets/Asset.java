package pl.luncher.v3.luncher_core.common.domain.assets;

import java.util.UUID;

public interface Asset {

  String getUploadUrl();

  void save();

  void delete();

  String getAccessUrl();

  UUID getAssetId();

  AssetPermissionsChecker permissions();
//
//  Place getPlace();
//
//  void setPlace(Place place);
}
