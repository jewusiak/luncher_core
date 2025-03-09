package pl.luncher.common.infrastructure.persistence;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.beans.factory.annotation.Autowired;
import pl.luncher.v3.luncher_core.application.configuration.properties.LuncherProperties;
import pl.luncher.v3.luncher_core.user.model.ForgottenPasswordIntent;

@Mapper(componentModel = ComponentModel.SPRING)
abstract class ForgottenPasswordIntentDbMapper {

  @Autowired
  protected LuncherProperties luncherProperties;

  public abstract ForgottenPasswordIntentDb toDb(ForgottenPasswordIntent forgottenPasswordIntent,
      UserDb user);

  @Mapping(target = "userId", source = "user.uuid")
  @Mapping(target = "baseApiUrl", expression = "java(luncherProperties.getBaseApiUrl())")
  public abstract ForgottenPasswordIntent toDomain(
      ForgottenPasswordIntentDb forgottenPasswordIntentDb);
}
