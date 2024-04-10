package pl.luncher.v3.luncher_core.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.luncher.v3.luncher_core.common.utils.AuthenticatedUserAttributeResolver;

import java.util.List;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final AuthenticatedUserAttributeResolver authenticatedUserAttributeResolver;
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedUserAttributeResolver);
    }
}
