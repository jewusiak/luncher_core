package pl.luncher.v3.luncher_core.user.domainservices.interfaces;

import java.util.UUID;
import pl.luncher.v3.luncher_core.user.model.ForgottenPasswordIntent;

public interface ForgottenPasswordIntentPersistenceService {

  ForgottenPasswordIntent save(ForgottenPasswordIntent forgottenPasswordIntent);

  ForgottenPasswordIntent findById(UUID id);
}
