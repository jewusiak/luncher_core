package pl.luncher.v3.luncher_core.common.model.responses;

import java.util.UUID;
import lombok.Value;

@Value
public class CreateLinkAssetResponse {

  UUID assetId;
  String uploadUrl;
  String accessUrl;
  UUID linkedPlaceId;

}
