package pl.luncher.v3.luncher_core.common.assets;

public interface Asset {

  String getUploadUrl();

  void save();

  void delete();

  String getAccessUri();
}
