package pl.luncher.v3.luncher_core.common.domain.users;

import java.util.UUID;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.requests.UserUpdateRequest;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.responses.BasicUserDataResponse;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.responses.FullUserDataResponse;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.responses.UserProfileResponse;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;
import pl.luncher.v3.luncher_core.common.persistence.models.UserDb;

public interface User {

  UUID getUuid();

  void save();

  void updateWith(UserUpdateRequest userUpdateRequest);

  void delete();

  UserPermissionsChecker permissions();

  AppRole getRole();

  FullUserDataResponse castToFullDataResponse();

  BasicUserDataResponse castToBasicDataResponse();

  UserDb getDbEntity();

  UserProfileResponse castToProfileResponse();
  
  void changePassword(String newPassword);
}
