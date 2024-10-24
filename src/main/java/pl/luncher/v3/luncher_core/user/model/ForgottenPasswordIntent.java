package pl.luncher.v3.luncher_core.user.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ForgottenPasswordIntent {

  private UUID id;
  private UUID userId;
  private LocalDateTime validityDate;
  private boolean used;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private String baseApiUrl;


  public String getResetUrl() {
    if (baseApiUrl == null || id == null) {
      return null;
    }
    return "%s/auth/resetpassword/%s".formatted(baseApiUrl, id);
  }


  public boolean isValid() {
    return LocalDateTime.now().isBefore(getValidityDate())
        && !isUsed();
  }

  public void throwIfNotValid() {
    if (!isValid()) {
      throw new ForgottenPasswordIntentInvalidException("Missing exception for this action.");
    }
  }

  public void invalidate() {
    this.setUsed(true);
  }
}
