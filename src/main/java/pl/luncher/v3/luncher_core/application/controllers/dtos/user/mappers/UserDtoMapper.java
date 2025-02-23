package pl.luncher.v3.luncher_core.application.controllers.dtos.user.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.application.controllers.dtos.user.requests.UserCreateRequest;
import pl.luncher.v3.luncher_core.application.controllers.dtos.user.requests.UserRegistrationRequest;
import pl.luncher.v3.luncher_core.application.controllers.dtos.user.requests.UserUpdateRequest;
import pl.luncher.v3.luncher_core.application.controllers.dtos.user.responses.UserBasicResponse;
import pl.luncher.v3.luncher_core.application.controllers.dtos.user.responses.UserProfileResponse;
import pl.luncher.v3.luncher_core.user.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING, nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface UserDtoMapper {

  // requests
  User toDomain(UserCreateRequest request);

  User toDomain(UserUpdateRequest request);

  User toDomain(UserRegistrationRequest request);

  // responses

  UserBasicResponse toUserBasicResponse(User user);

  UserProfileResponse toUserProfileResponse(User user);
}
