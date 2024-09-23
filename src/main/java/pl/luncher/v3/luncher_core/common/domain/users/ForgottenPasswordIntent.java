package pl.luncher.v3.luncher_core.common.domain.users;

import java.time.LocalDateTime;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.responses.CreatePasswordResetIntentResponse;

public interface ForgottenPasswordIntent {

  String getResetUri();

  LocalDateTime getValidityDate();
  
  boolean isValid();

  void throwIfNotValid();

  void invalidate();
  
  void save();
  
  User getUser();

  CreatePasswordResetIntentResponse castToCreatePasswordResetIntentResponse();
}
