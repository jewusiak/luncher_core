package pl.luncher.v3.luncher_core.application.controllers.dtos.assets.responses;

import java.util.UUID;
import lombok.Value;

@Value
public class CreateAssetResponse {

  UUID assetId;
  UUID placeId;
  String accessUrl;
  String status;

}
