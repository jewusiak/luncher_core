package pl.luncher.v3.luncher_core.user.domainservices;


import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.luncher.v3.luncher_core.user.domainservices.providers.BaseApiUrlGetter;
import pl.luncher.v3.luncher_core.user.domainservices.providers.PasswordRequestIntentValiditySecondsGetter;
import pl.luncher.v3.luncher_core.user.model.ForgottenPasswordIntent;
import pl.luncher.v3.luncher_core.user.model.User;

@Component
@RequiredArgsConstructor
class ForgottenPasswordIntentFactory {

  private final PasswordRequestIntentValiditySecondsGetter passwordRequestIntentValiditySecondsGetter;
  private final BaseApiUrlGetter baseApiUrlGetter;

  public ForgottenPasswordIntent of(User user) {
    return ForgottenPasswordIntent.builder().userId(user.getUuid()).used(false)
        .validityDate(generateValidityDate())
        .baseApiUrl(baseApiUrlGetter.getBaseApiUrl()).build();
  }

  private LocalDateTime generateValidityDate() {
    return LocalDateTime.now()
        .plusSeconds(
            passwordRequestIntentValiditySecondsGetter.getPasswordRequestIntentValiditySeconds());
  }
}
