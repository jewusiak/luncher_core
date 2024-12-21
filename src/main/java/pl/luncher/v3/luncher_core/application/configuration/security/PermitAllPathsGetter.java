package pl.luncher.v3.luncher_core.application.configuration.security;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
@RequiredArgsConstructor
public class PermitAllPathsGetter {

  private final ConfigurableApplicationContext context;

  public List<MethodPathPair> getAllPaths() {
    List<MethodPathPair> paths = new ArrayList<>();
    context.getBean(RequestMappingHandlerMapping.class).getHandlerMethods()
        .forEach((key, handlerMethod) -> {
          if (!handlerMethod.getBeanType().getPackageName().contains("pl.luncher")) {
            return;
          }

          if (handlerMethod.getMethod().getAnnotation(PermitAll.class) == null) {
            return;
          }

          String urlPath = key.getPathPatternsCondition().getPatterns().iterator().next()
              .getPatternString();

          urlPath = Pattern.compile("\\{\\w*\\}").matcher(urlPath).replaceAll("*");

          HttpMethod method = key.getMethodsCondition().getMethods().iterator().next()
              .asHttpMethod();

          MethodPathPair methodPathPair = new MethodPathPair(method, urlPath);
          if (!paths.contains(methodPathPair)) {
            paths.add(methodPathPair);
          }

        });

    return paths;
  }

  @Value
  @EqualsAndHashCode
  public static class MethodPathPair {

    HttpMethod method;
    String path;
  }
}
