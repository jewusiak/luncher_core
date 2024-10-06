package pl.luncher.v3.luncher_core.user.persistence.repositories;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.user.domainservices.ForgottenPasswordIntentPersistenceService;
import pl.luncher.v3.luncher_core.user.model.ForgottenPasswordIntent;
import pl.luncher.v3.luncher_core.user.persistence.model.mappers.ForgottenPasswordIntentDbMapper;

@RequiredArgsConstructor
@Service
class JpaForgottenPasswordIntentPersistenceService implements
    ForgottenPasswordIntentPersistenceService {

  private final ForgottenPasswordIntentRepository forgottenPasswordIntentRepository;
  private final UserRepository userRepository;
  private final ForgottenPasswordIntentDbMapper forgottenPasswordIntentDbMapper;

  @Override
  public ForgottenPasswordIntent save(ForgottenPasswordIntent forgottenPasswordIntent) {
    var user = userRepository.findUserByUuid(forgottenPasswordIntent.getUserId()).orElseThrow();
    var toBeSaved = forgottenPasswordIntentDbMapper.toDb(forgottenPasswordIntent, user);

    return forgottenPasswordIntentDbMapper.toDomain(
        forgottenPasswordIntentRepository.save(toBeSaved));
  }

  @Override
  public ForgottenPasswordIntent findById(UUID id) {
    return forgottenPasswordIntentRepository.findById(id)
        .map(forgottenPasswordIntentDbMapper::toDomain)
        .orElseThrow();
  }
}
