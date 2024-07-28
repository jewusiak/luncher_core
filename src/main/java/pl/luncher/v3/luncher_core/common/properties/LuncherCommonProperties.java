package pl.luncher.v3.luncher_core.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("pl.luncher.common")
@Getter
@Setter
public class LuncherCommonProperties {

  private String baseApiUrl;
  private int passwordRequestIntentValiditySeconds;
}
