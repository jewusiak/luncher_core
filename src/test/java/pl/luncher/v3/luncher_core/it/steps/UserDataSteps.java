package pl.luncher.v3.luncher_core.it.steps;

import io.cucumber.java.en.Given;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.luncher.v3.luncher_core.common.persistence.enums.AppRole;
import pl.luncher.v3.luncher_core.common.persistence.models.UserDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.UserRepository;

@RequiredArgsConstructor
public class UserDataSteps {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Given("User(s) exist(s):")
  public void userExists(List<Map<String, String>> data) {
    var users = data.stream().map(this::fromMap).toList();

    userRepository.saveAll(users);
  }

  private UserDb fromMap(Map<String, String> um) {
    return UserDb.builder().uuid(UUID.fromString(um.get("uuid"))).email(um.get("email"))
        .passwordHash(passwordEncoder.encode(um.get("password")))
        .enabled(true).role(AppRole.valueOf(um.get("role"))).firstName(um.get("name")).surname(um.get("surname"))
        .build();
  }
}
