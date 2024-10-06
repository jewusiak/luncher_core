package pl.luncher.v3.luncher_core.user.persistence.model.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.user.model.User;
import pl.luncher.v3.luncher_core.user.persistence.model.UserDb;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface UserDbMapper {

  UserDb toEntity(User user);

  User toDomain(UserDb userDb);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  UserDb partialUpdate(
      User user, @MappingTarget UserDb userDb);
}