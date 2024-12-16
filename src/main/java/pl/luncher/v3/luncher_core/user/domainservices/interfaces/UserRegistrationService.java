package pl.luncher.v3.luncher_core.user.domainservices.interfaces;

import jakarta.validation.constraints.NotBlank;
import pl.luncher.v3.luncher_core.user.model.User;

public interface UserRegistrationService {

  void registerNewUser(User user, @NotBlank String password);
}
