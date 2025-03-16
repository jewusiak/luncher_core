package pl.luncher.common.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JpaRepositoriesHelper {

  private final UserRepository userRepository;
  private final PlaceRepository placeRepository;
  private final PlaceTypeRepository placeTypeRepository;
  private final ForgottenPasswordIntentRepository forgottenPasswordIntentRepository;
  private final AssetRepository assetRepository;
  private final PageArrangementsRepository pageArrangementsRepository;


  public void deleteAll() {
    pageArrangementsRepository.deleteAll();
    assetRepository.deleteAll();
    placeRepository.deleteAll();
    forgottenPasswordIntentRepository.deleteAll();
    userRepository.deleteAll();
    placeTypeRepository.deleteAll();
  }
}
