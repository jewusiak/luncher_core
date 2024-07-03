package pl.luncher.v3.luncher_core.common.place;

import java.util.UUID;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminUpdatePlaceRequest;
import pl.luncher.v3.luncher_core.common.model.responses.FullPlaceResponse;
import pl.luncher.v3.luncher_core.common.model.responses.BasicPlaceResponse;
import pl.luncher.v3.luncher_core.common.assets.AssetToPlaceConnector;
import pl.luncher.v3.luncher_core.common.model.dto.OpeningWindowDto;

public interface Place {

  void save();

  void updateWith(AdminUpdatePlaceRequest request);

  BasicPlaceResponse castToBasicPlaceResponse();

  FullPlaceResponse castToFullPlaceResponse();

  void addOpeningWindow(OpeningWindowDto openingWindowDto);

  void removeOpeningWindow(UUID openingWindowId);

  UUID getPlaceId();

  AssetToPlaceConnector getAssetToPlaceConnectorWithRef();

  void delete();
}
