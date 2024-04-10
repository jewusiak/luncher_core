package pl.luncher.v3.luncher_core.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@SecurityScheme(
        scheme = "bearer",
        name = "Authorization",
        in = SecuritySchemeIn.COOKIE,
        bearerFormat = "JWT",
        type = SecuritySchemeType.HTTP
)
public class SwaggerConfiguration {

    @Bean
    public OpenAPI apiOpenApi() {
        OpenAPI api = new OpenAPI();
        api.setInfo(new Info().title("Luncher Core API").version("1.0.0"));
        List<Server> serverList = new ArrayList<>();
        serverList.add(new Server().url("http://localhost:8080").description("local luncher-core"));
        serverList.add(new Server().url("https://core.api.pre.luncher.pl").description("PRE luncher-core"));
        api.setServers(serverList);
        api.setSecurity(List.of(new SecurityRequirement().addList("Authorization")));
        return api;
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder().group("common").displayName("Common API").packagesToScan("pl.luncher.v3.luncher_core.common.controllers").build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder().group("admin").displayName("Admin API").packagesToScan("pl.luncher.v3.luncher_core.admin.controllers").build();
    }

}
