package pl.luncher.v3.luncher_core.user.domainservices;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.user.domainservices.interfaces.ForgottenPasswordIntentPersistenceService;
import pl.luncher.v3.luncher_core.user.domainservices.interfaces.ForgottenPasswordService;
import pl.luncher.v3.luncher_core.user.domainservices.interfaces.UserPersistenceService;
import pl.luncher.v3.luncher_core.user.model.ForgottenPasswordIntent;
import pl.luncher.v3.luncher_core.user.model.User;

@Service
@RequiredArgsConstructor
class ForgottenPasswordServiceImpl implements ForgottenPasswordService {

  private final ForgottenPasswordIntentPersistenceService forgottenPasswordIntentPersistenceService;
  private final ForgottenPasswordIntentFactory forgottenPasswordIntentFactory;
  private final UserPersistenceService userPersistenceService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public ForgottenPasswordIntent createPasswordResetIntent(String email) {
    User user = userPersistenceService.getByEmail(email);
    var passwordIntent = forgottenPasswordIntentFactory.of(user);

    return forgottenPasswordIntentPersistenceService.save(passwordIntent);
  }

  @Override
  public void resetWithToken(UUID uuid, String newPassword) {
    var passwordIntent = forgottenPasswordIntentPersistenceService.findById(uuid);
    passwordIntent.throwIfNotValid();

    var user = userPersistenceService.getById(passwordIntent.getUserId());
    user.setPasswordHash(passwordEncoder.encode(newPassword));
    userPersistenceService.save(user);

    passwordIntent.invalidate();

    forgottenPasswordIntentPersistenceService.save(passwordIntent);
  }
}
