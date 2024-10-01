package pl.luncher.v3.luncher_core.user.persistence.repositories;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class JpaUserPersistenceService {

  private final UserRepository userRepository;
  
  
}
