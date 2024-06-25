package pl.luncher.v3.luncher_core.common.assets;

import pl.luncher.v3.luncher_core.common.place.Place;

public interface Asset {

  String getUploadUrl();

  void save();

  void delete();

  String getAccessUrl();

  void linkToPlace(Place place);
}
