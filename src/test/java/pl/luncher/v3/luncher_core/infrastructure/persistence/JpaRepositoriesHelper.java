package pl.luncher.v3.luncher_core.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JpaRepositoriesHelper {

  private final UserRepository userRepository;
  private final PlaceRepository placeRepository;
  private final PlaceTypeRepository placeTypeRepository;
  private final ForgottenPasswordIntentRepository forgottenPasswordIntentRepository;


  public void deleteAll() {
    placeRepository.deleteAll();
    forgottenPasswordIntentRepository.deleteAll();
    userRepository.deleteAll();
    placeTypeRepository.deleteAll();
  }
}
