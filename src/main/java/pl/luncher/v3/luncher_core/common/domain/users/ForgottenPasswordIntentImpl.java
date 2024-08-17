package pl.luncher.v3.luncher_core.common.domain.users;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import pl.luncher.v3.luncher_core.common.model.responses.CreatePasswordResetIntentResponse;
import pl.luncher.v3.luncher_core.common.persistence.models.ForgottenPasswordIntentDb;
import pl.luncher.v3.luncher_core.common.persistence.repositories.ForgottenPasswordIntentRepository;

@AllArgsConstructor
class ForgottenPasswordIntentImpl implements ForgottenPasswordIntent {

  private ForgottenPasswordIntentDb forgottenPasswordIntentDb;
  private final String baseApiUrl;
  private final ForgottenPasswordIntentRepository forgottenPasswordIntentRepository;
  private final User user;

  @Override
  public String getResetUri() {
    return "%s/auth/resetpassword/%s".formatted(baseApiUrl,
        forgottenPasswordIntentDb.getRequestUuid());
  }

  @Override
  public LocalDateTime getValidityDate() {
    return forgottenPasswordIntentDb.getValidityDate();
  }

  @Override
  public boolean isValid() {
    return LocalDateTime.now().isBefore(forgottenPasswordIntentDb.getValidityDate())
        && !forgottenPasswordIntentDb.isUsed();
  }

  @Override
  public void throwIfNotValid() {
    if (!isValid()) {
      throw new ForgottenPasswordIntentInvalidException();
    }
  }

  @Override
  public void invalidate() {
    this.forgottenPasswordIntentDb.setUsed(true);
  }

  @Override
  public void save() {
    forgottenPasswordIntentDb = forgottenPasswordIntentRepository.save(forgottenPasswordIntentDb);
  }

  @Override
  public User getUser() {
    return user;
  }

  @Override
  public CreatePasswordResetIntentResponse castToCreatePasswordResetIntentResponse() {
    return new CreatePasswordResetIntentResponse(getResetUri(), getValidityDate().toString());
  }
}
