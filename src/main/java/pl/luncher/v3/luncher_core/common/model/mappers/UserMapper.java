package pl.luncher.v3.luncher_core.common.model.mappers;

import org.mapstruct.Mapper;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.model.responses.UserProfileResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserProfileResponse map(User user);
}
