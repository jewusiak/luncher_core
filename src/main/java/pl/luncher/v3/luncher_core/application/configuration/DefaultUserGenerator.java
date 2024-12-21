package pl.luncher.v3.luncher_core.application.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.user.domainservices.interfaces.UserPersistenceService;
import pl.luncher.v3.luncher_core.user.model.AppRole;
import pl.luncher.v3.luncher_core.user.model.User;

@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultUserGenerator {

  private final PasswordEncoder passwordEncoder;
  private final UserPersistenceService userPersistenceService;

  @EventListener(ApplicationReadyEvent.class)
  public void seedData() {
    var root = new User();
    root.setRole(AppRole.SYS_ROOT);
    root.setFirstName("Grzegorz");
    root.setSurname("Root");
    root.setEmail("root@luncher.pl");
    root.setPasswordHash(passwordEncoder.encode("1234"));
    root.setEnabled(true);
    try {
      userPersistenceService.save(root);
    } catch (RuntimeException exception) {
      log.info("Failed to save the user", exception);
    }

  }
}
