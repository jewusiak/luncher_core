package pl.luncher.v3.luncher_core.common.domain.place;

import java.util.UUID;
import pl.luncher.v3.luncher_core.common.domain.users.User;
import pl.luncher.v3.luncher_core.common.model.dto.OpeningWindowDto;
import pl.luncher.v3.luncher_core.common.model.requests.PlaceUpdateRequest;
import pl.luncher.v3.luncher_core.common.model.responses.BasicPlaceResponse;
import pl.luncher.v3.luncher_core.common.model.responses.FullPlaceResponse;
import pl.luncher.v3.luncher_core.common.persistence.models.PlaceDb;

public interface Place {

  void save();

  void updateWith(PlaceUpdateRequest request);

  BasicPlaceResponse castToBasicPlaceResponse();

  FullPlaceResponse castToFullPlaceResponse();

  void addOpeningWindow(OpeningWindowDto openingWindowDto);

  void removeOpeningWindow(UUID openingWindowId);

  UUID getPlaceId();

  PlacePermissionsChecker permissions();

  PlaceDb getDbEntity();

  void changeOwner(User newOwner);

  User getOwner();
}
