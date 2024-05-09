package pl.luncher.v3.luncher_core.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.List;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    scheme = "bearer",
    name = "Authorization",
    in = SecuritySchemeIn.COOKIE,
    bearerFormat = "JWT",
    type = SecuritySchemeType.HTTP
)
public class SwaggerConfiguration {

  @Value("${pl.luncher.swagger.apiCallBaseUrl}")
  private String apiCallBaseUrl;

  @Bean
  public OpenAPI apiOpenApi() {
    OpenAPI api = new OpenAPI();
    api.setInfo(new Info().title("Luncher Core API").version("1.0.0"));
    List<Server> serverList = new ArrayList<>();
    serverList.add(new Server().url(apiCallBaseUrl).description("predefined address - see config"));
    api.setServers(serverList);
    api.setSecurity(List.of(new SecurityRequirement().addList("Authorization")));
    return api;
  }

  @Bean
  public GroupedOpenApi authApi() {
    return GroupedOpenApi.builder().group("common").displayName("Common API")
        .packagesToScan("pl.luncher.v3.luncher_core.common.controllers").build();
  }

  @Bean
  public GroupedOpenApi adminPlacesApi() {
    return GroupedOpenApi.builder().group("admin-places").displayName("Admin (Places) API")
        .pathsToMatch("/admin/places/**", "/admin/places")
        .packagesToScan("pl.luncher.v3.luncher_core.admin.controllers").build();
  }

  @Bean
  public GroupedOpenApi adminUsersApi() {
    return GroupedOpenApi.builder().group("admin-users").displayName("Admin (Users) API")
        .pathsToMatch("/admin/users/**", "/admin/users")
        .packagesToScan("pl.luncher.v3.luncher_core.admin.controllers").build();
  }

  @Bean
  public GroupedOpenApi placesExplorationApi() {
    return GroupedOpenApi.builder().group("places-exploration")
        .displayName("Places exploration (end-users) API")
        .pathsToMatch("/places/exploration/**", "/places/exploration")
        .packagesToScan("pl.luncher.v3.luncher_core.common.controllers").build();
  }

}
