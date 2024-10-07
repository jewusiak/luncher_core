package pl.luncher.v3.luncher_core.controllers.dtos.user.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.controllers.dtos.user.requests.UserCreateRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.user.requests.UserRegistrationRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.user.requests.UserUpdateRequest;
import pl.luncher.v3.luncher_core.controllers.dtos.user.responses.UserBasicResponse;
import pl.luncher.v3.luncher_core.controllers.dtos.user.responses.UserProfileResponse;
import pl.luncher.v3.luncher_core.user.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface UserDtoMapper {

  // requests
  User toDomain(UserCreateRequest request, String passwordHash);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateDomain(@MappingTarget User user, UserUpdateRequest request, String passwordHash);

  User toDomain(UserRegistrationRequest request, String passwordHash);

  // responses

  UserBasicResponse toUserBasicResponse(User user);

  UserProfileResponse toUserProfileResponse(User user);
}