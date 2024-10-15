package pl.luncher.v3.luncher_core.it.steps;

import io.cucumber.java.en.And;
import lombok.RequiredArgsConstructor;
import pl.luncher.v3.luncher_core.it.steps.ParentSteps.EntityIdType;

@RequiredArgsConstructor
public class VariablesSteps {

  @And("Put ID {} idx {} to cache from HTTP response from path {}")
  public void putIdToCache(String variableName, String id, String path) {
    String value = ParentSteps.getCachedHttpResp().jsonPath().getString(path);
    ParentSteps.putIdToCache(value, Integer.parseInt(id), EntityIdType.valueOf(variableName));
  }

  @And("Put variable {} to cache from HTTP response from path {}")
  public void putVariableToCache(String variableName, String path) {
    String value = ParentSteps.getCachedHttpResp().jsonPath().getString(path);
    ParentSteps.putToCache(variableName, value);
  }
}
