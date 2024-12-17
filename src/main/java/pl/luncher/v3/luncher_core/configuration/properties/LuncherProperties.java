package pl.luncher.v3.luncher_core.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import pl.luncher.v3.luncher_core.infrastructure.filesystem.FilesystemPersistentAssetsBasePathGetter;
import pl.luncher.v3.luncher_core.user.domainservices.providers.BaseApiUrlGetter;
import pl.luncher.v3.luncher_core.user.domainservices.providers.PasswordRequestIntentValiditySecondsGetter;

@Configuration
@ConfigurationProperties("pl.luncher")
@Getter
@Setter
public class LuncherProperties implements BaseApiUrlGetter,
    PasswordRequestIntentValiditySecondsGetter, FilesystemPersistentAssetsBasePathGetter {

  private String baseApiUrl;
  private int passwordRequestIntentValiditySeconds;
  private String filesystemPersistentAssetsBasePath;


  @Override
  public String getFilesystemPersistentAssetsBasePathWithTrailingSlash() {
    return filesystemPersistentAssetsBasePath.endsWith("/") ? filesystemPersistentAssetsBasePath
        : (filesystemPersistentAssetsBasePath + "/");
  }

}
