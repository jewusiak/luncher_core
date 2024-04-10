package pl.luncher.v3.luncher_core.admin.model.mappers;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminCreateUserRequest;
import pl.luncher.v3.luncher_core.admin.model.requests.AdminUpdateUserRequest;
import pl.luncher.v3.luncher_core.admin.model.responses.AdminBasicUserDataResponse;
import pl.luncher.v3.luncher_core.admin.model.responses.AdminFullUserDataResponse;
import pl.luncher.v3.luncher_core.common.domain.infra.User;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class AdminUserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(source = ".", target = ".")
    public abstract AdminBasicUserDataResponse mapToBasic(User user);

    @Mapping(source = ".", target = ".")
    public abstract AdminFullUserDataResponse mapToFull(User user);

    @Mapping(source = "email", target = "email")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "surname", target = "surname")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "password", target = "passwordHash", qualifiedByName = "hashPassword")
    public abstract User map(AdminCreateUserRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Named("hashPassword")
    String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "changes.password", target = "passwordHash", qualifiedByName = "hashPassword")
    @Mapping(source = "userUuid", target = "uuid")
    public abstract void mapToUpdateUser(@MappingTarget User mappingTarget, AdminUpdateUserRequest changes, UUID userUuid);
}
