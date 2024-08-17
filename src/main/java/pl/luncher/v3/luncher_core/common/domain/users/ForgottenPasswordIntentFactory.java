package pl.luncher.v3.luncher_core.common.domain.users;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.common.persistence.models.ForgottenPasswordIntentDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.ForgottenPasswordIntentRepository;
import pl.luncher.v3.luncher_core.common.properties.LuncherCommonProperties;

@Component
@RequiredArgsConstructor
public class ForgottenPasswordIntentFactory {

  private final ForgottenPasswordIntentRepository forgottenPasswordIntentRepository;
  private final UserFactory userFactory;
  private final LuncherCommonProperties luncherCommonProperties;

  /**
   * Fetches existing intent
   */
  public ForgottenPasswordIntent pullFromRepo(UUID uuid) {
    ForgottenPasswordIntentDb dbEntity = forgottenPasswordIntentRepository.findById(uuid)
        .orElseThrow();

    return of(dbEntity);
  }

  /**
   * Creates new intent for user
   */
  public ForgottenPasswordIntent of(User user) {
    var dbEntity = ForgottenPasswordIntentDb.builder().used(false)
        .validityDate(LocalDateTime.now().plusSeconds(
            luncherCommonProperties.getPasswordRequestIntentValiditySeconds()))
        .user(user.getDbEntity()).build();
    return of(dbEntity);
  }

  private ForgottenPasswordIntent of(ForgottenPasswordIntentDb dbEntity) {
    return new ForgottenPasswordIntentImpl(dbEntity, luncherCommonProperties.getBaseApiUrl(),
        forgottenPasswordIntentRepository,
        userFactory.of(dbEntity.getUser()));
  }
}
