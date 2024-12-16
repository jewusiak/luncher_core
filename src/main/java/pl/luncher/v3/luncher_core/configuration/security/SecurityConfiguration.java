package pl.luncher.v3.luncher_core.configuration.security;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import pl.luncher.v3.luncher_core.auth.services.JwtAuthFilter;
import pl.luncher.v3.luncher_core.user.domainservices.interfaces.UserPersistenceService;
import pl.luncher.v3.luncher_core.user.model.AppRole;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

  private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);
  private final JwtAuthFilter jwtAuthFilter;
  private final PasswordEncoder passwordEncoder;
  private final PermitAllPathsGetter permitAllPathsGetter;
  @Value("${pl.luncher.swagger.accessible}")
  private String swaggerAccessible;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.authorizeHttpRequests(matcher -> {
      matcher.requestMatchers("/auth/**", "/error").permitAll();
      allowSwaggerConditionally(matcher);
      allowPermitAllAnnotatedMappings(matcher);
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

  private void allowPermitAllAnnotatedMappings(
      AuthorizeHttpRequestsConfigurer<?>.AuthorizationManagerRequestMatcherRegistry matcher) {
    permitAllPathsGetter.getAllPaths()
        .forEach(pair -> {
          log.trace("Permitting path {} {} via @PermitAll.", pair.getMethod(), pair.getPath());
          matcher.requestMatchers(pair.getMethod(), pair.getPath()).permitAll();
        });
  }

  private void allowSwaggerConditionally(
      AuthorizeHttpRequestsConfigurer<?>.AuthorizationManagerRequestMatcherRegistry matcher) {
    if (swaggerAccessible.equals("true")) {
      matcher.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();
    }
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    var roleHierarchy = new RoleHierarchyImpl();
    String hierarchy = String.join(" > ",
        Arrays.stream(AppRole.values()).map(AppRole::authorityName).toList());
    roleHierarchy.setHierarchy(hierarchy);
    return roleHierarchy;
  }

  @Bean
  public AuthenticationProvider authenticationProvider(
      UserPersistenceService userPersistenceService) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(
        userPersistenceService::getByEmail);
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
    var handler = new DefaultMethodSecurityExpressionHandler();
    handler.setRoleHierarchy(roleHierarchy());
    return handler;
  }
}
