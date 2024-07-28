package pl.luncher.v3.luncher_core.common.domain.users;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.luncher.v3.luncher_core.common.model.requests.UserCreateRequest;
import pl.luncher.v3.luncher_core.common.model.requests.UserRegistrationRequest;
import pl.luncher.v3.luncher_core.common.model.requests.UserUpdateRequest;
import pl.luncher.v3.luncher_core.common.model.responses.BasicUserDataResponse;
import pl.luncher.v3.luncher_core.common.model.responses.FullUserDataResponse;
import pl.luncher.v3.luncher_core.common.model.responses.UserProfileResponse;
import pl.luncher.v3.luncher_core.common.persistence.models.UserDb;

@Mapper(componentModel = "spring")
abstract class UserMapper {

  @Autowired
  private PasswordEncoder passwordEncoder;

  public abstract BasicUserDataResponse mapToBasic(UserDb user);

  public abstract FullUserDataResponse mapToFull(UserDb user);

  public abstract UserProfileResponse map(UserDb user);

  @Mapping(source = "email", target = "email")
  @Mapping(source = "firstName", target = "firstName")
  @Mapping(source = "surname", target = "surname")
  @Mapping(source = "role", target = "role")
  @Mapping(source = "password", target = "passwordHash", qualifiedByName = "hashPassword")
  @Mapping(source = "enabled", target = "enabled", defaultValue = "true")
  public abstract UserDb map(UserCreateRequest request);

  @Mapping(source = "email", target = "email")
  @Mapping(source = "firstName", target = "firstName")
  @Mapping(source = "surname", target = "surname")
  @Mapping(source = "password", target = "passwordHash", qualifiedByName = "hashPassword")
  @Mapping(target = "role", expression = "java(AppRole.USER)")
  @Mapping(target = "enabled", constant = "true")
  public abstract UserDb map(UserRegistrationRequest request);

  @BeanMapping(ignoreByDefault = true)
  @Named("hashPassword")
  String hashPassword(String plainPassword) {
    return passwordEncoder.encode(plainPassword);
  }

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  @Mapping(source = "changes.password", target = "passwordHash", qualifiedByName = "hashPassword")
  @Mapping(target = "uuid", ignore = true)
  public abstract void mapToUpdateUser(@MappingTarget UserDb mappingTarget,
      UserUpdateRequest changes);
}
