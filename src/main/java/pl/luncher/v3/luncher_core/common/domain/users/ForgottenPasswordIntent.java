package pl.luncher.v3.luncher_core.common.domain.users;

import java.time.LocalDateTime;

public interface ForgottenPasswordIntent {

  String getResetUri();

  LocalDateTime getValidityDate();
  
  boolean isValid();
  
  void invalidate();
  
  void save();
  
  User getUser();
}
