package pl.luncher.v3.luncher_core.user.domainservices.interfaces;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import pl.luncher.v3.luncher_core.user.model.User;

public interface UserManagementService {

  User getUserByUuid(UUID uuid);

  User createUser(User userToBeCreated, @NotNull @Size(min = 4) String password, User requestingUser);

  User updateUser(UUID userId, User changes, @Size(min = 4) String password, User requestingUser);

  void deleteUser(@NotNull UUID uuid, User requestingUser);
}
