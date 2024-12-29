package pl.luncher.v3.luncher_core.user.domainservices;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.luncher.v3.luncher_core.user.model.User;

@Mapper(componentModel = "spring")
interface UserUpdateMapper {

  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "authorities", ignore = true)
  @Mapping(target = "passwordHash", source = "passwordHash")
  @Mapping(target = "enabled", expression = "java(newUser.getEnabled()!=null ? newUser.getEnabled() : oldUser.getEnabled())")
  @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
  void updateUser(@MappingTarget User oldUser, User newUser, String passwordHash);
}
