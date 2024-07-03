package pl.luncher.v3.luncher_core.it.steps;

import io.cucumber.java.en.Given;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.luncher.v3.luncher_core.common.domain.infra.AppRole;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.repositories.UserRepository;

@RequiredArgsConstructor
public class UserDataSteps {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Given("User exists:")
  public void userExists(List<Map<String, String>> data) {
    var um = data.get(0);
    User user = User.builder().uuid(UUID.fromString(um.get("uuid"))).email(um.get("email"))
        .passwordHash(passwordEncoder.encode(um.get("password")))
        .enabled(true).role(AppRole.valueOf(um.get("role"))).firstName(um.get("name")).surname(um.get("surname"))
        .build();

    userRepository.save(user);
  }
}
