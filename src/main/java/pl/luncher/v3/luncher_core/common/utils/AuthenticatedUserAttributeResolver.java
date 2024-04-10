package pl.luncher.v3.luncher_core.common.utils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import pl.luncher.v3.luncher_core.common.exceptions.UserExtractionFromContextFailed;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.services.UserService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticatedUserAttributeResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        try {
            var userUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var user = userService.getUserByUuid(userUuid);
            if (user == null) throw new EntityNotFoundException();
            return user;
        } catch (Exception e) {
            log.info("Couldn't extract required user because of {}", e.toString());
            throw new UserExtractionFromContextFailed();
        }
    }
}
