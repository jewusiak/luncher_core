package pl.luncher.v3.luncher_core.user.domainservices;


import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.common.properties.LuncherCommonProperties;
import pl.luncher.v3.luncher_core.user.model.ForgottenPasswordIntent;
import pl.luncher.v3.luncher_core.user.model.User;

@Component
@RequiredArgsConstructor
public class ForgottenPasswordIntentFactory {

  private final LuncherCommonProperties luncherCommonProperties;

  public ForgottenPasswordIntent of(User user) {
    return ForgottenPasswordIntent.builder().userId(user.getUuid()).used(false)
        .validityDate(generateValidityDate())
        .baseApiUrl(luncherCommonProperties.getBaseApiUrl()).build();
  }

  private LocalDateTime generateValidityDate() {
    return LocalDateTime.now()
        .plusSeconds(luncherCommonProperties.getPasswordRequestIntentValiditySeconds());
  }
}
