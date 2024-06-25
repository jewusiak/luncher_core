package pl.luncher.v3.luncher_core.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("pl.luncher.gcp.objectstorage")
@Getter
@Setter
public class GcpObjectStorageProperties {

  private String gcpHost;
  private String bucketName;
  private String projectId;
  private String imagePathPrefix;

}
