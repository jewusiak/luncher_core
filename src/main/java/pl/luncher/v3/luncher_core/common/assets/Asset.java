package pl.luncher.v3.luncher_core.common.assets;

import java.util.UUID;
import pl.luncher.v3.luncher_core.common.place.Place;

public interface Asset {

  String getUploadUrl();

  void save();

  void delete();

  String getAccessUrl();

  UUID getAssetId();

  void setPlace(Place place);

  AssetPermissionsChecker permissions();
}
