package pl.luncher.v3.luncher_core.common.permissions;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionCheckerFactory {

  public WithUserPermissionContext withUser(UUID userId) {
    return new WithUserPermissionContextImpl(userId);
  }

}
