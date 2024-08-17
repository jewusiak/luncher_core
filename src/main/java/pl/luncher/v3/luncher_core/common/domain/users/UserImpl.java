package pl.luncher.v3.luncher_core.common.domain.users;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.luncher.v3.luncher_core.common.exceptions.DuplicateEntityException;
import pl.luncher.v3.luncher_core.common.model.requests.UserUpdateRequest;
import pl.luncher.v3.luncher_core.common.model.responses.BasicUserDataResponse;
import pl.luncher.v3.luncher_core.common.model.responses.FullUserDataResponse;
import pl.luncher.v3.luncher_core.common.model.responses.UserProfileResponse;
import pl.luncher.v3.luncher_core.common.persistence.JpaExceptionsHandler;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;
import pl.luncher.v3.luncher_core.common.persistence.models.UserDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.UserRepository;

@AllArgsConstructor
class UserImpl implements User, UserDetails {

  private UserDb userDb;

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UUID getUuid() {
    return userDb != null ? userDb.getUuid() : null;
  }

  @Override
  public void save() {
    userDb = JpaExceptionsHandler.proxySave(userRepository::save, userDb, _i -> new DuplicateEntityException("User with the same email address already exists in the system."));
  }

  @Override
  public void updateWith(UserUpdateRequest userUpdateRequest) {
    userMapper.mapToUpdateUser(userDb, userUpdateRequest);
  }

  @Override
  public void delete() {
    userRepository.delete(userDb);
  }

  @Override
  public UserPermissionsChecker permissions() {
    return new UserPermissionsCheckerImpl(this);
  }

  @Override
  public AppRole getRole() {
    return Optional.ofNullable(userDb).map(UserDb::getRole).orElse(null);
  }

  @Override
  public FullUserDataResponse castToFullDataResponse() {
    return userMapper.mapToFull(userDb);
  }

  @Override
  public BasicUserDataResponse castToBasicDataResponse() {
    return userMapper.mapToBasic(userDb);
  }

  @Override
  public UserDb getDbEntity() {
    return userDb;
  }

  @Override
  public UserProfileResponse castToProfileResponse() {
    return userMapper.map(userDb);
  }

  @Override
  public void changePassword(String newPassword) {
    userDb.setPasswordHash(passwordEncoder.encode(newPassword));
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof User user && user.getUuid() != null) {
      return user.getUuid().equals(this.getUuid());
    }

    return false;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    var roles = getRole();
    if (roles == null) {
      roles = AppRole.USER;
    }
    return List.of(roles.authorityObj());
  }

  @Override
  public String getPassword() {
    return Optional.ofNullable(userDb).map(UserDb::getPasswordHash).orElse(null);
  }

  @Override
  public String getUsername() {
    return Optional.ofNullable(userDb).map(UserDb::getEmail).orElse(null);
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    //todo: implement locking the account
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return Optional.ofNullable(userDb).map(UserDb::isEnabled).orElse(true);
  }

}
