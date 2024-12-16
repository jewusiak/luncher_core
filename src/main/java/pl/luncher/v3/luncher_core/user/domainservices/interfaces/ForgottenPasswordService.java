package pl.luncher.v3.luncher_core.user.domainservices.interfaces;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import pl.luncher.v3.luncher_core.user.model.ForgottenPasswordIntent;

public interface ForgottenPasswordService {

  ForgottenPasswordIntent createPasswordResetIntent(@Email @NotNull String email);

  void resetWithToken(UUID uuid, @NotBlank String newPassword);
}
