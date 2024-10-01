package pl.luncher.v3.luncher_core.configuration;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import pl.luncher.v3.luncher_core.common.domain.users.User;
import pl.luncher.v3.luncher_core.user.domainservices.UserFactory;
import pl.luncher.v3.luncher_core.common.exceptions.UserExtractionFromContextFailed;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticatedUserAttributeResolver implements HandlerMethodArgumentResolver {

  private final UserFactory userFactory;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(User.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    try {
      return Optional.ofNullable(SecurityContextHolder.getContext())
          .map(SecurityContext::getAuthentication)
          .map(Authentication::getPrincipal)
          .map(UUID.class::cast)
          .map(userFactory::pullFromRepo)
          .orElse(null);
    } catch (Exception e) {
      log.info("Couldn't extract required user because of {}", e.toString());
      throw new UserExtractionFromContextFailed();
    }
  }
}
