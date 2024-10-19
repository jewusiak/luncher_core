package pl.luncher.v3.luncher_core.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import pl.luncher.v3.luncher_core.common.properties.LuncherCommonProperties;
import pl.luncher.v3.luncher_core.configuration.filters.SwaggerOutputFilter;

@Configuration
@SecurityScheme(
    scheme = "bearer",
    name = "Authorization",
    in = SecuritySchemeIn.COOKIE,
    bearerFormat = "JWT",
    type = SecuritySchemeType.HTTP
)
@RequiredArgsConstructor
@Slf4j
public class SwaggerConfiguration {

  private final LuncherCommonProperties luncherCommonProperties;
  private final ConfigurableApplicationContext context;
  private final SwaggerOutputFilter swaggerOutputFilter;

  @Bean
  public OpenAPI apiOpenApi() {
    OpenAPI api = new OpenAPI();
    api.setInfo(new Info().title("Luncher Core API").version("1.1.0"));
    List<Server> serverList = new ArrayList<>();
    serverList.add(new Server().url(luncherCommonProperties.getBaseApiUrl()).description("predefined address - see config"));
    api.setServers(serverList);
    api.setSecurity(List.of(new SecurityRequirement().addList("Authorization")));
    return api;
  }


  public void setUpGroupedOpenApis() {
    log.info("Loading OpenApis...");
    ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
    BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
    context.getBean(RequestMappingHandlerMapping.class).getHandlerMethods().forEach((key, value) -> {
      if (!value.getBeanType().getPackageName().contains("pl.luncher")) {
        return;
      }

      String httpMethod = key.getMethodsCondition().getMethods().iterator().next().name();
      String urlPath = key.getPathPatternsCondition().getPatterns().iterator().next().getPatternString();
      String pckg = value.getBeanType().getPackageName();
      Method controllerMethod = value.getMethod();

      injectGroupedApiBean(httpMethod, urlPath, pckg, controllerMethod, registry);
    });

  }


  public void injectGroupedApiBean(String httpMethod, String urlPath, String pckg, Method controllerMethod,
      BeanDefinitionRegistry registry) {
    String urlSafePath = urlPath.replace("/", "_").replace("{", "").replace("}", "");

    String docsGroupName = "%s__%s".formatted(httpMethod, urlSafePath);
    String groupDisplayName = "%s %s".formatted(httpMethod, urlPath);

    var builder = GroupedOpenApi.builder().group(docsGroupName)
        .displayName(groupDisplayName).packagesToScan(pckg)
        .pathsToMatch(urlPath)
        .addOpenApiMethodFilter(controllerMethod::equals);

    registry.registerBeanDefinition(docsGroupName,
        BeanDefinitionBuilder.genericBeanDefinition(GroupedOpenApi.class).addConstructorArgValue(builder)
            .getBeanDefinition());
  }

  @Bean
  public GroupedOpenApi authApi() {
    setUpGroupedOpenApis();
    return GroupedOpenApi.builder().group("dummy").displayName("dummy").pathsToMatch("/nonexistent/path").build();
  }

  @Bean
  public FilterRegistrationBean<SwaggerOutputFilter> swaggerOutputRegistrationBean() {
    FilterRegistrationBean<SwaggerOutputFilter> bean = new FilterRegistrationBean<>();
    bean.setFilter(swaggerOutputFilter);
    bean.addUrlPatterns("/v3/api-docs/*");
    return bean;
  }

}
