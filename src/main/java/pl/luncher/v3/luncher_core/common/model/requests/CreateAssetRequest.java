package pl.luncher.v3.luncher_core.common.model.requests;

import lombok.Value;

@Value
public class CreateAssetRequest {

  String name;
  String description;
  String fileExtension;
  AssetParentType parentType;
  String parentRef;


  public enum AssetParentType {
    PLACE
  }
}
