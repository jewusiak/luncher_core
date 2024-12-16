package pl.luncher.v3.luncher_core.configuration;

import net.iakovlev.timeshape.TimeZoneEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeZoneResolverConfig {

  @Bean
  public TimeZoneEngine timeZoneEngine() {
    return TimeZoneEngine.initialize();
  }
}
