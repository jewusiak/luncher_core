package pl.luncher.v3.luncher_core.presentation.controllers.dtos.user.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.user.responses.BasicUserResponse;
import pl.luncher.v3.luncher_core.user.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface UserDtoMapper {

  User toEntity(BasicUserResponse basicUserResponse);

  BasicUserResponse toDto(User user);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  User partialUpdate(
      BasicUserResponse basicUserResponse, @MappingTarget User user);
}