package pl.luncher.v3.luncher_core.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.luncher.v3.luncher_core.common.domain.infra.AppRole;
import pl.luncher.v3.luncher_core.common.jwtUtils.JwtAuthFilter;
import pl.luncher.v3.luncher_core.common.services.UserService;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserService userService;
    @Value("${pl.luncher.swagger.accessible}") private String swaggerAccessible;

    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(matcher -> {
            matcher.requestMatchers("/auth/**", "/error").permitAll();
            allowSwaggerConditionally(matcher);
            matcher.anyRequest().authenticated();
        });

        http.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(config -> {
            config.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        });

        http.cors(config -> {
            CorsConfiguration configuration = new CorsConfiguration();
//            configuration.setAllowedOrigins(Arrays.asList("*"));
            configuration.setAllowedOriginPatterns(List.of("*"));
            configuration.setAllowedMethods(List.of("*"));
            configuration.setAllowedHeaders(List.of("*"));
            configuration.setAllowCredentials(true);
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            config.configurationSource(source);
        });
        return http.build();
    }

    private void allowSwaggerConditionally(AuthorizeHttpRequestsConfigurer<?>.AuthorizationManagerRequestMatcherRegistry matcher) {
        if (swaggerAccessible.equals("true")) {
            matcher.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();
        }
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        var roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = String.join(" > ", Arrays.stream(AppRole.values()).map(AppRole::authorityName).toList());
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService::getUserDetailsByEmail);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        var handler = new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy());
        return handler;
    }
}
