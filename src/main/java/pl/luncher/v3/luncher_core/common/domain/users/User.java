package pl.luncher.v3.luncher_core.common.domain.users;

import java.util.UUID;
import pl.luncher.v3.luncher_core.common.model.requests.UserUpdateRequest;
import pl.luncher.v3.luncher_core.common.model.responses.BasicUserDataResponse;
import pl.luncher.v3.luncher_core.common.model.responses.FullUserDataResponse;
import pl.luncher.v3.luncher_core.common.model.responses.UserProfileResponse;
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
  
  ForgottenPasswordIntent requestForgottenPassword();
  
  void changePassword(String newPassword);
}
