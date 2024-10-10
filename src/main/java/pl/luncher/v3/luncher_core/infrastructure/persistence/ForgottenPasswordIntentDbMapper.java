package pl.luncher.v3.luncher_core.infrastructure.persistence;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.beans.factory.annotation.Autowired;
import pl.luncher.v3.luncher_core.common.properties.LuncherCommonProperties;
import pl.luncher.v3.luncher_core.user.model.ForgottenPasswordIntent;

@Mapper(componentModel = ComponentModel.SPRING)
abstract class ForgottenPasswordIntentDbMapper {

  @Autowired
  protected LuncherCommonProperties luncherCommonProperties;

  public abstract ForgottenPasswordIntentDb toDb(ForgottenPasswordIntent forgottenPasswordIntent,
      UserDb user);

  @Mapping(target = "userId", source = "user.uuid")
  @Mapping(target = "baseApiUrl", expression = "java(luncherCommonProperties.getBaseApiUrl())")
  public abstract ForgottenPasswordIntent toDomain(
      ForgottenPasswordIntentDb forgottenPasswordIntentDb);
}