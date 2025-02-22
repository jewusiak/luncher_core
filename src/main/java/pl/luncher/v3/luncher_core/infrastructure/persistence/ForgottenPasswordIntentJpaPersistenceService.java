package pl.luncher.v3.luncher_core.infrastructure.persistence;

import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.luncher.v3.luncher_core.user.domainservices.interfaces.ForgottenPasswordIntentPersistenceService;
import pl.luncher.v3.luncher_core.user.model.ForgottenPasswordIntent;

@RequiredArgsConstructor
@Service
class ForgottenPasswordIntentJpaPersistenceService implements
    ForgottenPasswordIntentPersistenceService {

  private final ForgottenPasswordIntentRepository forgottenPasswordIntentRepository;
  private final UserRepository userRepository;
  private final ForgottenPasswordIntentDbMapper forgottenPasswordIntentDbMapper;

  @Override
  public ForgottenPasswordIntent save(ForgottenPasswordIntent forgottenPasswordIntent) {
    var user = userRepository.findUserByUuid(forgottenPasswordIntent.getUserId()).orElseThrow(
        () -> new NoSuchElementException(
            "No user with ID %s is found.".formatted(forgottenPasswordIntent.getUserId())));
    var toBeSaved = forgottenPasswordIntentDbMapper.toDb(forgottenPasswordIntent, user);

    return forgottenPasswordIntentDbMapper.toDomain(
        forgottenPasswordIntentRepository.save(toBeSaved));
  }

  @Override
  public ForgottenPasswordIntent findById(UUID id) {
    return forgottenPasswordIntentRepository.findById(id)
        .map(forgottenPasswordIntentDbMapper::toDomain)
        .orElseThrow(
            () -> new NoSuchElementException("Password Intent %s not found!".formatted(id)));
  }
}
