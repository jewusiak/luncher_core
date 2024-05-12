package pl.luncher.v3.luncher_core.common.place;

import java.util.UUID;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminUpdatePlaceRequest;
import pl.luncher.v3.luncher_core.admin.model.responses.AdminBasicPlaceResponse;
import pl.luncher.v3.luncher_core.admin.model.responses.AdminFullPlaceResponse;
import pl.luncher.v3.luncher_core.common.place.valueobject.OpeningWindowDto;

public interface Place {

  void save();

  void updateWith(AdminUpdatePlaceRequest request);

  AdminBasicPlaceResponse castToAdminBasicPlaceResponse();

  AdminFullPlaceResponse castToAdminFullPlaceResponse();

  void addOpeningWindow(OpeningWindowDto openingWindowDto);

  void removeOpeningWindow(UUID openingWindowId);

}
