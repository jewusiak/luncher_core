package pl.luncher.common.infrastructure.persistence;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.user.model.AppRole;

@RequiredArgsConstructor
@Component
public class UserRepositoryHelper {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;


  public void saveUsersFromMap(List<Map<String, String>> data) {
    var users = data.stream().map(this::fromMap).toList();

    userRepository.saveAll(users);
  }

  private UserDb fromMap(Map<String, String> um) {
    return UserDb.builder().uuid(UUID.fromString(um.get("uuid"))).email(um.get("email"))
        .passwordHash(passwordEncoder.encode(um.get("password")))
        .enabled(true).role(AppRole.valueOf(um.get("role"))).firstName(um.get("name"))
        .surname(um.get("surname"))
        .build();
  }
}
