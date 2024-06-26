package pl.luncher.v3.luncher_core.common.model.requests;

import lombok.Value;

@Value
public class CreateAssetRequest {

  String name;
  String description;
  String fileExtension;
  String placeId;

}
