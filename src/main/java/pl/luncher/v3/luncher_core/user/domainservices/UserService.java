package pl.luncher.v3.luncher_core.user.domainservices;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.user.domainservices.interfaces.UserManagementService;
import pl.luncher.v3.luncher_core.user.domainservices.interfaces.UserPersistenceService;
import pl.luncher.v3.luncher_core.user.domainservices.interfaces.UserRegistrationService;
import pl.luncher.v3.luncher_core.user.model.AppRole;
import pl.luncher.v3.luncher_core.user.model.User;

@Service
@RequiredArgsConstructor
class UserService implements UserRegistrationService, UserManagementService {

  private final PasswordEncoder passwordEncoder;
  private final UserPersistenceService userPersistenceService;
  private final UserUpdateMapper userUpdateMapper;

  @Override
  public void registerNewUser(User user, String password) {
    user.setEnabled(true);
    user.setRole(AppRole.USER);
    user.setPasswordHash(passwordEncoder.encode(password));

    user.validate();
    userPersistenceService.save(user);
  }

  @Override
  public User getUserByUuid(UUID uuid) {
    return userPersistenceService.getById(uuid);
  }

  @Override
  public User createUser(User userToBeCreated, String password, User requestingUser) {

    userToBeCreated.setPasswordHash(passwordEncoder.encode(password));

    userToBeCreated.permissions().byUser(requestingUser).createThisUser().throwIfNotPermitted();

    userToBeCreated.validate();
    return userPersistenceService.save(userToBeCreated);
  }

  @Override
  public User updateUser(UUID userId, User changes, String password, User requestingUser) {
    var user = userPersistenceService.getById(userId);

    userUpdateMapper.updateUser(user, changes, password == null ? null : passwordEncoder.encode(password));

    user.permissions().byUser(requestingUser).edit().throwIfNotPermitted();

    user.validate();
    return userPersistenceService.save(user);
  }

  @Override
  public void deleteUser(UUID uuid, User requestingUser) {
    var user = userPersistenceService.getById(uuid);

    user.permissions().byUser(requestingUser).delete().throwIfNotPermitted();

    userPersistenceService.deleteById(uuid);
  }
}
