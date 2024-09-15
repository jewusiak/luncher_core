package pl.luncher.v3.luncher_core.common.domain.assets;

import java.util.UUID;
import pl.luncher.v3.luncher_core.common.domain.place.model.Place;

public interface Asset {

  String getUploadUrl();

  void save();

  void delete();

  String getAccessUrl();

  UUID getAssetId();

  AssetPermissionsChecker permissions();

  Place getPlace();

  void setPlace(Place place);
}
